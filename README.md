# SkaETL

SkaLogs ETL

## How-Tos

#### How to add a new transformation for the process consumers

**Core**
- Add the transformation type in `io.skalogs.skaetl.domain.TypeValidation.java` file

**BackEnd**
- Add a new Maven dependency in `pom.xml` file (Optional)
-

**FrontEnd**
- Add the transformation type in `add/Transformation.vue`
- Modify the `actionView` method in `add/Transformation.vue`
- Add the transformation type in `edit/Transformation.vue`


**ProcessImporterImpl**
- Create the new Transformator implementation in `io.skalogs.skaetl.service` package
- Add the new transformator in `io.skalogs.skaetl.service.GenericTransformator.java.init()`

