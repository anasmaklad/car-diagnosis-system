# Car Diagnosis System

A sophisticated car diagnosis system that uses graph visualization to identify and display potential car issues based on symptoms. The system provides an interactive visualization of the relationships between vehicles, symptoms, and potential issues.

## Features

- Interactive graph visualization of car issues and symptoms
- Real-time diagnosis based on input symptoms
- Severity-based issue categorization
- Modern, responsive UI with dark theme
- Detailed issue descriptions and recommendations
- Score-based issue matching (50% threshold)
- Dynamic force-directed graph layout
- Zoom and pan capabilities
- Interactive node and edge highlighting

## Prerequisites

- Scala 3.x
- Python 3.x (for the visualization server)
- sbt (Scala Build Tool)

## Project Structure

```
car-diagnosis-system/
├── src/
│   └── main/
│       └── scala/
│           └── CarDiagnosisSystem.scala
├── car_input_case.txt
├── car_diagnosis_graph.html
├── server.py
├── build.sbt
└── README.md
```

## Setup and Installation

1. Clone the repository:
```bash
git clone https://github.com/anasmaklad/car-diagnosis-system.git
cd car-diagnosis-system
```

2. Run the application:
```bash
scala CarDiagnosisSystem.scala
```

The system will:
- Read the input from `car_input_case.txt`
- Generate a diagnosis
- Create a JSON file with the graph data
- Start a local server
- Open the visualization in your default browser

## Input Format

The input file (`car_input_case.txt`) should follow this format:
```
make=[Make]
model=[Model]
year=[Year]
mileage=[Mileage]
symptoms=[Symptom1, Symptom2, ...]
```

Example:
```
make=Toyota
model=Camry
year=2018
mileage=75000
symptoms=high temperature gauge, steam from hood, rough idle, check engine light
```

## Visualization Features

- **Node Types**:
  - Vehicle (Green)
  - Car Issues (Red)
  - Symptoms (Blue)

- **Edge Types**:
  - Issue connections (Red gradient)
  - Symptom connections (Blue gradient)
  - Include connections (Green gradient)

- **Interactive Features**:
  - Hover over nodes to see details
  - Click and drag to move nodes
  - Zoom in/out with mouse wheel
  - Reset view button
  - Edge highlighting on hover

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- D3.js for the graph visualization
- Scala for the backend logic
- Python for the local server 