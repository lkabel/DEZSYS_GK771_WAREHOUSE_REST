### XML-Mapping mit Spring Boot und Jackson

**1. Abh�ngigkeit**

Zur Serialisierung von Java-Objekten nach XML wird die Bibliothek `jackson-dataformat-xml` ben�tigt.

*   **Gradle (`build.gradle.kts`):**
    ```kotlin
    dependencies {  
        implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")  
    }
    ```

**2. REST-Controller**

Ein Endpunkt wird so konfiguriert, dass er den MIME-Typ `application/xml` produziert.

```java
@GetMapping(
    value = "/warehouse/{inID}/xml",
    produces = MediaType.APPLICATION_XML_VALUE
)
public ResponseEntity<WarehouseData> warehouseDataXml(@PathVariable String inID){
    return ResponseEntity.ok(service.getWarehouseData(inID));
}
```

**3. Datenmodell und Annotationen**

Die Java-Klasse wird mit Jackson-Annotationen versehen, um die XML-Struktur zu definieren.

```java
@JacksonXmlRootElement(localName = "warehouseData")  
public class WarehouseData {  
  
    @JacksonXmlProperty(localName = "warehouseID")  
    private String warehouseID;
    
    @JacksonXmlElementWrapper(localName = "productData")  
	@JacksonXmlProperty(localName = "product")  
	private Product[] productData;
    
    // �ffentliche Getter f�r alle Felder sind erforderlich.
}
```

**Erl�uterung der Annotationen:**

*   **`@JacksonXmlRootElement(localName = "warehouseData")`**: Definiert das XML-Root Element als `<warehouseData>`.
*   **`@JacksonXmlProperty(localName = "warehouseID")`**: Benennt das XML-Element f�r ein einzelnes Feld, z.B. `<warehouseID>`.
*   **`@JacksonXmlElementWrapper(localName = "productData")`**: Erzeugt ein umschlie�endes Element (`<productData>`) f�r eine Collection (Array/Liste).
*   **`@JacksonXmlProperty(localName = "product")`**: Benennt die einzelnen Elemente innerhalb der umschlossenen Collection (`<product>`).

**4. Funktionsweise in Spring**

*   **`HttpMessageConverter`**: Spring Boot konfiguriert automatisch den `MappingJackson2XmlHttpMessageConverter`, wenn `jackson-dataformat-xml` im Classpath vorhanden ist.
*   **Content-Type**: Anhand der `produces`-Angabe im Controller (`application/xml`) w�hlt Spring diesen Converter.
*   **`XmlMapper`**: Der Converter nutzt intern Jacksons `XmlMapper`, um das Java-Objekt anhand der Annotationen in einen XML-String zu serialisieren.

**5. Beispielhafte Datenerzeugung**

F�r die Serialisierung m�ssen die Felder des Objekts Werte enthalten. �ffentliche Getter-Methoden sind f�r diesen Prozess zwingend erforderlich.

```java
products[3] = new Product();
products[3].setProductID("00-316253");
products[3].setProductName("Persil Discs Color");
products[3].setProductCategory("Waschmittel");
products[3].setProductQuantity(1430);
products[3].setProductUnit("Packung 700G");
```