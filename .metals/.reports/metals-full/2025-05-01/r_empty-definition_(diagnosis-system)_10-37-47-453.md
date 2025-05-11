error id: 
file:///D:/project/simple_uroura/diagnosis-system/src/main/scala/diagnosis-system.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 502
uri: file:///D:/project/simple_uroura/diagnosis-system/src/main/scala/diagnosis-system.scala
text:
```scala
import scala.io.StdIn.readLine

object DiagnosisSystem {

  // Models
  case class Disease(name: String, symptoms: List[String])
  case class Patient(name: String, age: Int, gender: String, symptoms: List[String])

  def main(args: Array[String]): Unit = {

    // === Expert Knowledge ===
    val expertDiseases: List[Disease] = List(
      Disease("Heart Failure", List("shortness of breath", "fatigue", "swollen ankles", "rapid heartbeat")),
      Disease("Angina", List("chest pain", "@@shortness of breath", "nausea")),
      Disease("Asthma", List("shortness of breath", "coughing", "wheezing", "chest tightness")),
      Disease("Pneumonia", List("fever", "coughing", "chills", "shortness of breath", "fatigue")),
      Disease("COVID-19", List("fever", "coughing", "loss of taste", "shortness of breath", "fatigue")),
      Disease("Migraine", List("headache", "nausea", "sensitivity to light", "blurred vision")),
      Disease("Diabetes", List("frequent urination", "increased thirst", "fatigue", "blurred vision")),
      Disease("Anemia", List("fatigue", "pale skin", "dizziness", "shortness of breath"))
    )

    // === User Input ===
    println("Enter patient name:")
    val name = readLine()

    println("Enter patient age:")
    val age = readLine().toInt

    println("Enter patient gender:")
    val gender = readLine()

    println("Enter patient symptoms (comma-separated):")
    val symptomsInput = readLine()
    val symptoms = symptomsInput.split(",").map(_.trim.toLowerCase).toList

    val patient = Patient(name, age, gender, symptoms)

    println(s"\nDiagnosing patient: ${patient.name}, Age: ${patient.age}, Gender: ${patient.gender}")
    println(s"Reported symptoms: ${patient.symptoms.mkString(", ")}\n")

    val matches = matchDiseasesWithScore(patient, expertDiseases)
      .filter { case (_, _, score) => score >= 0.3 }
      .sortBy { case (_, _, score) => -score }

    if (matches.isEmpty) {
      println("No strong disease matches found.")
    } else {
      println("Possible diagnoses (ranked by score):")
      for ((disease, matchedSymptoms, score) <- matches) {
        val percent = (score * 100).toInt
        println(s"- ${disease.name} ($percent% match) — matched symptoms: ${matchedSymptoms.mkString(", ")}")
      }
    }
  }

  // Match and score disease-symptom matches
  def matchDiseasesWithScore(patient: Patient, diseases: List[Disease]): List[(Disease, List[String], Double)] = {
    diseases.flatMap { disease =>
      val matched = disease.symptoms.intersect(patient.symptoms)
      if (matched.nonEmpty) {
        val score = matched.length.toDouble / disease.symptoms.length
        Some((disease, matched, score))
      } else {
        None
      }
    }
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 