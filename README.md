# DroneApp

DroneApp is a Spring Boot application designed to manage drone operations, including registering drones, loading medications, and monitoring deliveries.

## Prerequisites
- **Java Development Kit (JDK) 17**: Ensure JDK 17 is installed. You can download it from the [official Oracle website](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).
- **IntelliJ IDEA**: Install [IntelliJ IDEA](https://www.jetbrains.com/idea/) as the development environment.
- **Postman**: To test the API endpoints, install [Postman](https://www.postman.com/downloads/).
- **Git**: To clone the repository, ensure Git is installed. Download it from the [official Git website](https://git-scm.com/downloads).

## Clone the Repository

Clone the repository using the following command:

```bash
git clone https://github.com/raizar24/DroneApp.git
```

## Open the Project in IntelliJ IDEA
- Open IntelliJ IDEA.
- Click File > Open and navigate to the DroneApp directory.
- Click OK to open the project.
- IntelliJ will automatically detect and import the Maven dependencies.
- Wait for the indexing process to complete.
- In the Maven window, click Reload All Maven Projects to ensure all dependencies are resolved.
- Navigate to `DroneAppApplication.java` and click Run to start the application.
- By default, the application runs on `http://localhost:8080`.

## Running JUnit Tests
Before testing the API endpoints, ensure that the JUnit tests are executed:
- Navigate to the test directory in the project.
- Right-click on the test package and select Run `All Tests`.
- Ensure that all tests pass successfully before proceeding.

## Scheduler for Drone Status Updates
The application includes a scheduler that runs at a `10-seconds` interval. This scheduler automatically updates the drone `status`:
- From `DELIVERING` to `DELIVERED`.
- From `DELIVERED` to `RETURNING`.

This ensures that the drone workflow transitions smoothly through the different states.

## API Endpoints

### Drones
Get All Drones
- Endpoint: `GET /api/drones`
- Retrieves a list of all registered drones.
- Example Response:
  ```json
    {
        "serialNumber": "DRONE001",
        "droneModel": "LIGHTWEIGHT",
        "weightLimit": 100,
        "batteryCapacity": 100,
        "droneState": "IDLE"
    }
  ```

### Get Available Drones
- Endpoint: `GET /api/drones/available`
- Retrieves drones that are available for loading.
- Example Response:
  ```json
    {
        "serialNumber": "DRONE001",
        "droneModel": "LIGHTWEIGHT",
        "weightLimit": 100,
        "batteryCapacity": 100,
        "droneState": "IDLE"
    }
  ```

### Get Loaded Medications By Serial Number
- Endpoint: `GET /api/drones/medications?serialNumber=DRONE001`
- Retrieves the medications loaded onto a specific `serialNumber`.
- Example Response:
  ```json
    {
        "id": 1,
        "drone": {
            "serialNumber": "DRONE001",
            "droneModel": "LIGHTWEIGHT",
            "weightLimit": 100,
            "batteryCapacity": 100,
            "droneState": "LOADING"
        },
        "medication": {
            "id": 1,
            "name": "Paracetamol",
            "weight": 20,
            "code": "CODE1",
            "image": "image1.jpg"
        },
        "quantity": 1,
        "totalWeight": 20
    }
  ```

### Get Drone Battery Level
- Endpoint: `GET /api/drones/battery?serialNumber=DRONE001`
- Retrieves the battery level of a specific `serialNumber`.
- Example Response:
  ```json
    {
      "serialNumber": "DRONE001",
      "batteryPercentage": 100
    }
  ```

### Register Drone
- Endpoint: `POST /api/drones/register`
- Registers a new drone in the system.
- Payload:
  ```json
  {
    "serialNumber": "DRONE999",
    "droneModel": "LIGHTWEIGHT",
    "weightLimit": 500,
    "batteryCapacity": 100,
    "droneState": "IDLE"
  }
  ```

### Load Drone with Medications
- Endpoint: `POST /api/drones/load`
- Loads a drone with medications.
- Payload:
  ```json
  {
    "serialNumber": "DRONE001",
    "medicationId": 1,
    "quantity": 1
  }
  ```
### Place Delivery
- Endpoint: `POST /api/drones/placeDelivery`
- Initiates a delivery using a loaded drone.
- Payload:
  ```json
  {
    "serialNumber": "DRONE001"
  }
  ```

## Medications

### Get All Medications
- Endpoint: `GET /api/medications`
- Retrieves all available medications.
- Example Response:
  ```json
    {
        "id": 1,
        "name": "Paracetamol",
        "weight": 20,
        "code": "CODE1",
        "image": "image1.jpg"
    },
  ```

### Get Medication By ID
- Endpoint: `GET /api/medications?id=1`
- Retrieves details of a specific medication by `id`.
- Example Response:
  ```json
  {
    "medication": {
        "id": 1,
        "name": "Paracetamol",
        "weight": 20,
        "code": "CODE1",
        "image": "image1.jpg"
    },
    "status": "success"
  }
  ```

### Add new Medication.
- Endpoint: `PUT /api/medications`
- adds new medication.
- Payload:
  ```json
  {
    "name": "Paracetamol",
    "weight": 10,
    "code": "PARA500",
    "image": "image-url"
  }
  ```
## Importing Postman Collection

To simplify API testing, import the following Postman collection JSON into Postman:
This will allow you to quickly test all API endpoints.
```json
{
	"info": {
		"_postman_id": "c406c426-b428-4b88-8d3a-ba69b9c9296c",
		"name": "DroneApp",
		"schema": "https://schema.getpostman.com/json/collection/v2.0.0/collection.json",
		"_exporter_id": "29795064"
	},
	"item": [
		{
			"name": "GetAllDrones",
			"request": {
				"auth": {
					"type": "noauth"
				},
				"method": "GET",
				"header": [],
				"url": "localhost:8080/api/drones"
			},
			"response": []
		},
		{
			"name": "GetAvailableDrones",
			"request": {
				"method": "GET",
				"header": [],
				"url": "localhost:8080/api/drones/available"
			},
			"response": []
		},
		{
			"name": "GetLoadedMedicationsBySerialNo",
			"protocolProfileBehavior": {
				"disableBodyPruning": true
			},
			"request": {
				"auth": {
					"type": "noauth"
				},
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "localhost:8080/api/drones/medications?serialNumber=DRONE001",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"drones",
						"medications"
					],
					"query": [
						{
							"key": "serialNumber",
							"value": "DRONE001"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "GetBattery",
			"protocolProfileBehavior": {
				"disableBodyPruning": true
			},
			"request": {
				"auth": {
					"type": "noauth"
				},
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/drones/battery?serialNumber=DRONE001",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"drones",
						"battery"
					],
					"query": [
						{
							"key": "serialNumber",
							"value": "DRONE001"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "GetAllMedications",
			"request": {
				"auth": {
					"type": "noauth"
				},
				"method": "GET",
				"header": [],
				"url": "http://localhost:8080/api/medications"
			},
			"response": []
		},
		{
			"name": "GetMedicationById",
			"request": {
				"auth": {
					"type": "noauth"
				},
				"method": "GET",
				"header": [],
				"url": {
					"raw": "localhost:8080/api/medications?id=1",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"medications"
					],
					"query": [
						{
							"key": "id",
							"value": "1"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "LoadDrone",
			"request": {
				"auth": {
					"type": "noauth"
				},
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"serialNumber\": \"DRONE001\",\r\n    \"medicationId\": 1,\r\n    \"quantity\": 1\r\n}\r\n",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": "localhost:8080/api/drones/load"
			},
			"response": []
		},
		{
			"name": "PlaceDelivery",
			"request": {
				"auth": {
					"type": "noauth"
				},
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"serialNumber\": \"DRONE001\"\r\n}\r\n",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": "localhost:8080/api/drones/placeDelivery"
			},
			"response": []
		},
		{
			"name": "RegisterDrone",
			"request": {
				"auth": {
					"type": "noauth"
				},
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"serialNumber\": \"DRONE999\",\r\n    \"droneModel\": \"LIGHTWEIGHT\",\r\n    \"weightLimit\": 500,\r\n    \"batteryCapacity\": 100,\r\n    \"droneState\": \"IDLE\"\r\n}\r\n",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": "localhost:8080/api/drones/register"
			},
			"response": []
		},
		{
			"name": "PutMedicine",
			"request": {
				"auth": {
					"type": "noauth"
				},
				"method": "PUT",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n  \"name\": \"Paracetamol\",\r\n  \"weight\": 10,\r\n  \"code\": \"PARA500\",\r\n  \"image\": \"image-url\"\r\n}\r\n",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": "http://localhost:8080/api/medications"
			},
			"response": []
		}
	]
}
```

# Preloaded Data in H2 In-Memory Database
## Drone Table
| Serial Number | Drone Model   | Weight Limit (g) | Battery Capacity (%) | State |
|---------------|---------------|-------------------|-----------------------|-------|
| DRONE001      | LIGHTWEIGHT   | 100               | 100                   | IDLE  |
| DRONE002      | MIDDLEWEIGHT  | 250               | 75                    | IDLE  |
| DRONE003      | CRUISERWEIGHT | 400               | 50                    | IDLE  |
| DRONE004      | HEAVYWEIGHT   | 1000              | 25                    | IDLE  |
| DRONE005      | HEAVYWEIGHT   | 1000              | 25                    | IDLE  |
| DRONE006      | LIGHTWEIGHT   | 100               | 100                   | IDLE  |
| DRONE007      | MIDDLEWEIGHT  | 250               | 75                    | IDLE  |
| DRONE008      | CRUISERWEIGHT | 400               | 50                    | IDLE  |
| DRONE009      | HEAVYWEIGHT   | 1000              | 25                    | IDLE  |
| DRONE010      | CRUISERWEIGHT | 400               | 50                    | IDLE  |

## Medication Table
| ID | Name         | Weight (g) | Code   | Image       |
|----|--------------|------------|--------|-------------|
| 1  | Paracetamol  | 20         | CODE1  | image1.jpg  |
| 2  | Ibuprofen    | 30         | CODE2  | image2.jpg  |
| 3  | Aspirin      | 40         | CODE3  | image3.jpg  |
| 4  | Methformin   | 50         | CODE4  | image4.jpg  |

