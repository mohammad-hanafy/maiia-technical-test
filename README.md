# MAIIA - Backend Technical Test
To work with the app you have two options
- Docker
- Build Artifacts

## Docker
First, make sure you have installed docker and docker-compose in your machine, to run the whole application, run the following command

```bash
docker-compose up -d
```
to follow the logs use the command

```bash
docker-compose logs -f -t
```
You can access the **backend** through: https://localhost:8080 and the **frontend** through http://localhost:3000

## Build Artifacts
### Pre-Requisites
- Java
- Gradle
- npm/yarn

### Backend
The backend is a SpringBoot project using `Gradle`. You can open it in your favorite IDE and run it as a SpringBoot app.

You can also run it through your CLI

You can run the app with the following command
```bash
./gradlew bootRun
```

If you want to run unit tests in CLI : 

```bash
./gradlew test
```

### Frontend
First, you need to `cd` into the right folder

```bash
cd ./client
```

Then install the project dependencies:

```bash
yarn install
```

Finnaly you can run the development server:

```bash
yarn dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see instructions for the technical test.
By default, all API calls will be redirected to http://localhost:8080

## Editor

The instructions work best with Visual Studio Code which provides an API to open local files directly from the browser by clicking a link.

If you favor another text editor providing a similar API feel free to modify `src/components/EditorLink.tsx` to suit your needs.


## Implemented Tasks

- The required tasks of **Implementing a service to generate availabilities of a practitioner** and **Creating appointments**  are completed.
- Adding Unit Test for **create appointment service**
- Implementing **DTO Design Pattern**.
-  Adding Error Handling for REST.
- Adding the ability to build the app using docker-compose.