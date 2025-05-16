error id: 4ADC6AE3BE5AF0C45A7E6FF398A55C4E
file:///D:/project/simple_uroura/diagnosis-system/src/main/scala/diagnosis-system.scala
### scala.reflect.internal.FatalError: no context found for source-file:///D:/project/simple_uroura/diagnosis-system/src/main/scala/diagnosis-system.scala,line-3,offset=101

occurred in the presentation compiler.



action parameters:
offset: 101
uri: file:///D:/project/simple_uroura/diagnosis-system/src/main/scala/diagnosis-system.scala
text:
```scala
scala-cli run diagnosis-system.scala \
  --dep io.circe::circe-generic:0.14.13 \
  --dep io.circe::@@circe-parser:0.14.13

import io.circe.generic.auto._
import io.circe.syntax._
import java.io.PrintWriter

object DiagnosisSystem {

  case class Disease(name: String, symptoms: List[String])
  case class Patient(name: String, age: Int, gender: String, symptoms: List[String])
  case class Node(id: String, label: String, nodeType: String)
  case class Edge(source: String, target: String, label: String)
  case class GraphData(nodes: List[Node], edges: List[Edge])

  def main(args: Array[String]): Unit = {
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

    println("Enter patient data (key=value), then press Enter twice:")
    val input = Iterator.continually(scala.io.StdIn.readLine())
      .takeWhile(_.nonEmpty).mkString("\n")

    val patient = parsePatientFromKVInput(input)

    println(s"\nDiagnosing patient: ${patient.name}")
    println(s"Symptoms: ${patient.symptoms.mkString(", ")}\n")

    val matches = matchDiseasesWithScore(patient, expertDiseases)
      .filter { case (_, _, score) => score >= 0.3 }
      .sortBy { case (_, _, score) => -score }

    if (matches.isEmpty) println("No strong matches found.")
    else {
      println("Possible diagnoses:")
      for ((disease, matchedSymptoms, score) <- matches) {
        println(s"- ${disease.name} (${(score * 100).toInt}%) matched: ${matchedSymptoms.mkString(", ")}")
      }
    }

    // Graph generation
    val nodes = scala.collection.mutable.Set[Node]()
    val edges = scala.collection.mutable.Set[Edge]()

    val patientId = s"patient_${patient.name.replaceAll(" ", "_")}"
    nodes += Node(patientId, patient.name, "Patient")

    patient.symptoms.foreach { symptom =>
      val sid = s"symptom_${symptom.replaceAll(" ", "_")}"
      nodes += Node(sid, symptom, "Symptom")
      edges += Edge(patientId, sid, "has")
    }

    matches.foreach { case (disease, _, score) =>
      val did = s"disease_${disease.name.replaceAll(" ", "_")}"
      nodes += Node(did, disease.name, "Disease")
      edges += Edge(patientId, did, f"possible (${score * 100}%.0f%%)")

      disease.symptoms.foreach { symptom =>
        val sid = s"symptom_${symptom.replaceAll(" ", "_")}"
        nodes += Node(sid, symptom, "Symptom")
        edges += Edge(did, sid, "includes")
      }
    }

    val graphData = GraphData(nodes.toList, edges.toList)
    val graphJson = graphData.asJson.spaces2

    println("\nGraph Representation (JSON):")
    println(graphJson)

    val writer = new PrintWriter("diagnosis_graph.json")
    writer.write(graphJson)
    writer.close()
  }

  def parsePatientFromKVInput(input: String): Patient = {
    val lines = input.split("\n").map(_.trim).filter(_.nonEmpty)
    val kv = lines.map { line =>
      val Array(k, v) = line.split("=", 2)
      (k.trim.toLowerCase, v.trim)
    }.toMap

    val name = kv("name")
    val age = kv("age").toInt
    val gender = kv("gender")
    val symptoms = kv("symptoms").split(",").map(_.trim).toList

    Patient(name, age, gender, symptoms)
  }

  def matchDiseasesWithScore(patient: Patient, diseases: List[Disease]): List[(Disease, List[String], Double)] = {
    diseases.flatMap { disease =>
      val matched = disease.symptoms.intersect(patient.symptoms)
      if (matched.nonEmpty)
        Some((disease, matched, matched.length.toDouble / disease.symptoms.length))
      else None
    }
  }
}

```


presentation compiler configuration:
Scala version: 2.13.13
Classpath:
<WORKSPACE>\diagnosis-system\.bloop\diagnosis-system\bloop-bsp-clients-classes\classes-Metals-2euyphYITci682W4QFVJZg== [exists ], <HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.4\semanticdb-javac-0.10.4.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.13\scala-library-2.13.13.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-core_2.13\0.14.3\circe-core_2.13-0.14.3.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-generic_2.13\0.14.3\circe-generic_2.13-0.14.3.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-parser_2.13\0.14.3\circe-parser_2.13-0.14.3.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-numbers_2.13\0.14.3\circe-numbers_2.13-0.14.3.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-core_2.13\2.8.0\cats-core_2.13-2.8.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\chuusai\shapeless_2.13\2.3.10\shapeless_2.13-2.3.10.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-jawn_2.13\0.14.3\circe-jawn_2.13-0.14.3.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-kernel_2.13\2.8.0\cats-kernel_2.13-2.8.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\jawn-parser_2.13\1.4.0\jawn-parser_2.13-1.4.0.jar [exists ]
Options:
-Yrangepos -Xplugin-require:semanticdb -release 11




#### Error stacktrace:

```
scala.tools.nsc.interactive.CompilerControl.$anonfun$doLocateContext$1(CompilerControl.scala:100)
	scala.tools.nsc.interactive.CompilerControl.doLocateContext(CompilerControl.scala:100)
	scala.tools.nsc.interactive.CompilerControl.doLocateContext$(CompilerControl.scala:99)
	scala.tools.nsc.interactive.Global.doLocateContext(Global.scala:114)
	scala.meta.internal.pc.PcDefinitionProvider.definitionTypedTreeAt(PcDefinitionProvider.scala:181)
	scala.meta.internal.pc.PcDefinitionProvider.definition(PcDefinitionProvider.scala:69)
	scala.meta.internal.pc.PcDefinitionProvider.definition(PcDefinitionProvider.scala:17)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$definition$1(ScalaPresentationCompiler.scala:477)
```
#### Short summary: 

scala.reflect.internal.FatalError: no context found for source-file:///D:/project/simple_uroura/diagnosis-system/src/main/scala/diagnosis-system.scala,line-3,offset=101