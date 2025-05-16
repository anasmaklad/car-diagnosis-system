//> using dep "io.circe::circe-generic:0.14.13"
//> using dep "io.circe::circe-parser:0.14.13"

import io.circe.generic.auto._
import io.circe.syntax._
import java.io.PrintWriter
import scala.io.StdIn.readLine

object DiagnosisSystem {

  case class Disease(name: String, symptoms: List[String])
  case class Patient(name: String, age: Int, gender: String, symptoms: List[String])
  case class Node(id: String, label: String, nodeType: String)
  case class Edge(source: String, target: String, label: String)
  
  case class GraphData(nodes: List[Node], edges: List[Edge])

  def main(args: Array[String]): Unit = {
    println("Enter patient data (key=value), then press Enter twice:")

    val input = Iterator
      .continually(readLine())
      .takeWhile(line => line != "")
      .toList

    val data = input.flatMap { line =>
      line.split("=", 2) match {
        case Array(k, v) => Some((k.trim.toLowerCase, v.trim))
        case _           => None
      }
    }.toMap

    val patient = Patient(
      name = data.getOrElse("name", "Unknown"),
      age = data.getOrElse("age", "0").toInt,
      gender = data.getOrElse("gender", "Unknown"),
      symptoms = data.getOrElse("symptoms", "").split(",").map(_.trim).toList
    )

    val expertDiseases = List(
      Disease("Heart Failure", List("shortness of breath", "fatigue", "swollen ankles", "rapid heartbeat")),
      Disease("Angina", List("chest pain", "shortness of breath", "nausea")),
      Disease("Asthma", List("shortness of breath", "coughing", "wheezing", "chest tightness")),
      Disease("Pneumonia", List("fever", "coughing", "chills", "shortness of breath", "fatigue")),
      Disease("COVID-19", List("fever", "coughing", "loss of taste", "shortness of breath", "fatigue")),
      Disease("Migraine", List("headache", "nausea", "sensitivity to light", "blurred vision")),
      Disease("Diabetes", List("frequent urination", "increased thirst", "fatigue", "blurred vision")),
      Disease("Anemia", List("fatigue", "pale skin", "dizziness", "shortness of breath"))
    )

    println(s"\nDiagnosing: ${patient.name}")
    println(s"Symptoms: ${patient.symptoms.mkString(", ")}\n")

    val matches = matchDiseasesWithScore(patient, expertDiseases)
      .filter(_._3 >= 0.3)
      .sortBy(-_._3)

    if (matches.isEmpty) println("No strong matches found.")
    else {
      println("Possible diagnoses:")
      for ((disease, matched, score) <- matches)
        println(s"- ${disease.name} (${(score * 100).toInt}%) matched: ${matched.mkString(", ")}")
    }

    // Graph
    val nodes = scala.collection.mutable.Set[Node]()
    val edges = scala.collection.mutable.Set[Edge]()

    val pid = "patient_" + patient.name.replace(" ", "_")
    nodes += Node(pid, patient.name, "Patient")
    for (sym <- patient.symptoms) {
      val sid = "symptom_" + sym.replace(" ", "_")
      nodes += Node(sid, sym, "Symptom")
      edges += Edge(pid, sid, "has")
    }

    for ((disease, _, score) <- matches) {
      val did = "disease_" + disease.name.replace(" ", "_")
      nodes += Node(did, disease.name, "Disease")
      edges += Edge(pid, did, f"possible (${score * 100}%.0f%%)")

      for (sym <- disease.symptoms) {
        val sid = "symptom_" + sym.replace(" ", "_")
        nodes += Node(sid, sym, "Symptom")
        edges += Edge(did, sid, "includes")
      }
    }

    val graphData = GraphData(nodes.toList, edges.toList)
    val graphJson = graphData.asJson.spaces2

    println("\nGraph JSON:")
    println(graphJson)

    val out = new PrintWriter("diagnosis_graph.json")
    out.write(graphJson)
    out.close()
  }

  def matchDiseasesWithScore(patient: Patient, diseases: List[Disease]): List[(Disease, List[String], Double)] = {
    diseases.flatMap { d =>
      val matched = d.symptoms.intersect(patient.symptoms)
      if (matched.nonEmpty)
        Some((d, matched, matched.length.toDouble / d.symptoms.length))
      else None
    }
  }
}
