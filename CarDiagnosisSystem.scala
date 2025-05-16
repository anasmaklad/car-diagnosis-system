//> using dep "io.circe::circe-generic:0.14.13"
//> using dep "io.circe::circe-parser:0.14.13"

import io.circe.generic.auto._
import io.circe.syntax._
import java.io.PrintWriter
import scala.io.Source
import java.awt.Desktop
import java.net.URI
import java.io.File
import java.nio.file.{Paths, Files}
import sys.process._

object CarDiagnosisSystem {

  case class CarIssue(name: String, symptoms: List[String], severity: String, description: String)
  case class Vehicle(model: String, year: Int, make: String, symptoms: List[String], mileage: Int)
  case class Node(id: String, label: String, nodeType: String)
  case class Edge(source: String, target: String, label: String)
  case class GraphData(nodes: List[Node], edges: List[Edge])

  def main(args: Array[String]): Unit = {
    try {
      // Read input from file
      val inputLines = Source.fromFile("car_input_case.txt").getLines().toList

      val data = inputLines.flatMap { line =>
        line.split("=", 2) match {
          case Array(k, v) => Some((k.trim.toLowerCase, v.trim))
          case _           => None
        }
      }.toMap

      val vehicle = Vehicle(
        model = data.getOrElse("model", "Unknown"),
        year = data.getOrElse("year", "0").toInt,
        make = data.getOrElse("make", "Unknown"),
        symptoms = data.getOrElse("symptoms", "").split(",").map(_.trim).toList,
        mileage = data.getOrElse("mileage", "0").toInt
      )

      val commonIssues = List(
        CarIssue(
          "Engine Overheating",
          List("high temperature gauge", "steam from hood", "coolant leak", "engine noise", "hot engine smell"),
          "High",
          "Engine overheating can cause severe damage if not addressed immediately. Check coolant levels and cooling system."
        ),
        CarIssue(
          "Brake Problems",
          List("squeaking brakes", "vibration when braking", "soft brake pedal", "brake warning light", "grinding noise"),
          "High",
          "Brake issues are critical for safety. Inspect brake pads, rotors, and fluid levels."
        ),
        CarIssue(
          "Battery Issues",
          List("slow engine crank", "dim lights", "battery warning light", "electrical problems", "corroded terminals"),
          "Medium",
          "Battery problems can leave you stranded. Check battery age, connections, and charging system."
        ),
        CarIssue(
          "Transmission Problems",
          List("slipping gears", "rough shifting", "transmission fluid leak", "check engine light", "delayed engagement"),
          "High",
          "Transmission issues can be expensive to repair. Check fluid levels and quality."
        ),
        CarIssue(
          "Tire Issues",
          List("low tire pressure", "uneven wear", "vibration while driving", "tire warning light", "bald spots"),
          "Medium",
          "Tire problems affect handling and safety. Regular rotation and alignment checks are important."
        ),
        CarIssue(
          "Oil Problems",
          List("oil warning light", "low oil pressure", "oil leak", "engine noise", "oil smell"),
          "High",
          "Oil issues can lead to engine damage. Check oil level and quality regularly."
        ),
        CarIssue(
          "Electrical System",
          List("dead battery", "flickering lights", "power window issues", "radio problems", "fuse blowing"),
          "Medium",
          "Electrical problems can affect multiple systems. Check alternator and battery connections."
        ),
        CarIssue(
          "Fuel System",
          List("poor fuel economy", "engine stalling", "rough idle", "check engine light", "fuel smell"),
          "Medium",
          "Fuel system issues affect performance and efficiency. Check fuel filter and injectors."
        ),
        CarIssue(
          "Suspension Problems",
          List("bumpy ride", "uneven tire wear", "pulling to one side", "noise over bumps", "sagging"),
          "Medium",
          "Suspension issues affect handling and comfort. Check shocks, struts, and alignment."
        ),
        CarIssue(
          "Exhaust System",
          List("loud exhaust", "exhaust smell", "reduced power", "check engine light", "rust spots"),
          "Medium",
          "Exhaust problems can affect performance and emissions. Check for leaks and damage."
        ),
        CarIssue(
          "AC/Heating System",
          List("weak airflow", "strange odors", "no cold air", "no hot air", "unusual noises"),
          "Low",
          "Climate control issues affect comfort. Check refrigerant levels and system components."
        ),
        CarIssue(
          "Steering Problems",
          List("loose steering", "steering wheel vibration", "power steering fluid leak", "hard steering", "noise when turning"),
          "High",
          "Steering issues affect vehicle control. Check power steering fluid and components."
        ),
        CarIssue(
          "Engine Misfire",
          List("rough idle", "loss of power", "check engine light", "engine vibration", "poor acceleration"),
          "High",
          "Misfires affect performance and emissions. Check spark plugs and ignition system."
        ),
        CarIssue(
          "Cooling System",
          List("overheating", "coolant leak", "low coolant", "heater not working", "sweet smell"),
          "High",
          "Cooling system issues can cause engine damage. Check radiator and hoses."
        ),
        CarIssue(
          "Timing Belt/Chain",
          List("engine won't start", "engine noise", "misfiring", "rough running", "check engine light"),
          "High",
          "Timing issues can cause severe engine damage. Regular replacement is crucial."
        )
      )

      println(s"\nDiagnosing: ${vehicle.make} ${vehicle.model} (${vehicle.year})")
      println(s"Mileage: ${vehicle.mileage}")
      println(s"Symptoms: ${vehicle.symptoms.mkString(", ")}\n")

      val matches = matchIssuesWithScore(vehicle, commonIssues)
        .filter(_._3 >= 0.5)
        .sortBy(-_._3)

      if (matches.isEmpty) println("No strong matches found (score >= 50%).")
      else {
        println("Possible issues (score >= 50%):")
        for ((issue, matched, score) <- matches) {
          println(s"\n- ${issue.name} (${(score * 100).toInt}%)")
          println(s"  Severity: ${issue.severity}")
          println(s"  Description: ${issue.description}")
          println(s"  Matched symptoms: ${matched.mkString(", ")}")
        }
      }

      // Graph
      val nodes = scala.collection.mutable.Set[Node]()
      val edges = scala.collection.mutable.Set[Edge]()

      val vid = "vehicle_" + vehicle.model.replace(" ", "_")
      nodes += Node(vid, s"${vehicle.make} ${vehicle.model}\n(${vehicle.year}, ${vehicle.mileage} miles)", "Vehicle")
      
      // Add matched symptoms
      val matchedSymptoms = matches.flatMap(_._2).toSet
      for (sym <- matchedSymptoms) {
        val sid = "symptom_" + sym.replace(" ", "_")
        nodes += Node(sid, sym, "Symptom")
        edges += Edge(vid, sid, "has")
      }

      // Add matched issues with their severity
      for ((issue, matched, score) <- matches) {
        val iid = "issue_" + issue.name.replace(" ", "_")
        nodes += Node(iid, s"${issue.name}\n(${issue.severity})", "Issue")
        edges += Edge(vid, iid, f"possible (${score * 100}%.0f%%)")

        // Connect issues to their matched symptoms
        for (sym <- matched) {
          val sid = "symptom_" + sym.replace(" ", "_")
          edges += Edge(iid, sid, "includes")
        }
      }

      val graphData = GraphData(nodes.toList, edges.toList)
      val graphJson = graphData.asJson.spaces2

      println("\nGenerating graph data...")
      println(s"Number of nodes: ${nodes.size}")
      println(s"Number of edges: ${edges.size}")

      // Save JSON file
      val jsonFile = new File("car_diagnosis_graph.json")
      val out = new PrintWriter(jsonFile)
      out.write(graphJson)
      out.close()
      println(s"JSON file saved to: ${jsonFile.getAbsolutePath}")

      // Ensure HTML file exists
      val htmlFile = new File("car_diagnosis_graph.html")
      if (!htmlFile.exists()) {
        println("Error: HTML file not found!")
        return
      }

      // Start the Python server
      println("\nStarting web server...")
      val serverProcess = Process("python server.py").run()
      
      // Wait for user to press Enter to stop the server
      println("\nPress Enter to stop the server...")
      scala.io.StdIn.readLine()
      
      // Stop the server
      serverProcess.destroy()
      println("Server stopped.")

    } catch {
      case e: Exception =>
        println(s"\nError: ${e.getMessage}")
        e.printStackTrace()
    }
  }

  def matchIssuesWithScore(vehicle: Vehicle, issues: List[CarIssue]): List[(CarIssue, List[String], Double)] = {
    issues.flatMap { i =>
      val matched = i.symptoms.intersect(vehicle.symptoms)
      if (matched.nonEmpty)
        Some((i, matched, matched.length.toDouble / i.symptoms.length))
      else None
    }
  }
}
