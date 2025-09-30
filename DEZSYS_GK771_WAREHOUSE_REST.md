### XML-Mapping mit Spring Boot und Jackson

**1. Abhängigkeit**

Zur Serialisierung von Java-Objekten nach XML wird die Bibliothek
`jackson-dataformat-xml` benötigt.

* **Gradle (`build.gradle.kts`):**

  ```kotlin
  dependencies {  
      implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")  
  }
  ```

**2. REST-Controller**

Ein Endpunkt wird so konfiguriert, dass er den MIME-Typ `application/xml`
produziert.

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

Die Java-Klasse wird mit Jackson-Annotationen versehen, um die XML-Struktur zu
definieren.

```java
@JacksonXmlRootElement(localName = "warehouseData")  
public class WarehouseData {  
  
    @JacksonXmlProperty(localName = "warehouseID")  
    private String warehouseID;
    
    @JacksonXmlElementWrapper(localName = "productData")  
    @JacksonXmlProperty(localName = "product")  
    private Product[] productData;
    
    // Öffentliche Getter für alle Felder sind erforderlich.
}
```

**Erläuterung der Annotationen:**

* **`@JacksonXmlRootElement(localName = "warehouseData")`**: Definiert das
  XML-Root Element als `<warehouseData>`.
* **`@JacksonXmlProperty(localName = "warehouseID")`**: Benennt das XML-Element
  für ein einzelnes Feld, z.B. `<warehouseID>`.
* **`@JacksonXmlElementWrapper(localName = "productData")`**: Erzeugt ein
  umschließendes Element (`<productData>`) für eine Collection (Array/Liste).
* **`@JacksonXmlProperty(localName = "product")`**: Benennt die einzelnen
  Elemente innerhalb der umschlossenen Collection (`<product>`).

**4. Funktionsweise in Spring**

* **`HttpMessageConverter`**: Spring Boot konfiguriert automatisch den
  `MappingJackson2XmlHttpMessageConverter`, wenn `jackson-dataformat-xml` im
  Classpath vorhanden ist.
* **Content-Type**: Anhand der `produces`-Angabe im Controller
  (`application/xml`) wählt Spring diesen Converter.
* **`XmlMapper`**: Der Converter nutzt intern Jacksons `XmlMapper`, um das
  Java-Objekt anhand der Annotationen in einen XML-String zu serialisieren.

**5. Beispielhafte Datenerzeugung**

Für die Serialisierung müssen die Felder des Objekts Werte enthalten.
Öffentliche Getter-Methoden sind für diesen Prozess zwingend erforderlich.

```java
products[3] = new Product();
products[3].setProductID("00-316253");
products[3].setProductName("Persil Discs Color");
products[3].setProductCategory("Waschmittel");
products[3].setProductQuantity(1430);
products[3].setProductUnit("Packung 700G");
```

# Dash-Client für Warehouse-Daten

Dieses Skript zeigt Lagerdaten aus einem Spring-Boot-Backend in einer
interaktiven Tabelle.

## Aufbau

* **Eingabefeld**: Eingabe einer `warehouseID`.
* **DataTable**: Anzeige der Produkte mit Sortier-, Filter- und Seitenfunktion.

## Funktionsweise

1. Benutzer gibt eine `warehouseID` ein.
2. Das Skript ruft `http://localhost:8080/warehouse/{warehouseID}/json` auf.
3. JSON wird in einen `pandas.DataFrame` umgewandelt.
4. Produkte und Lagerinfos erscheinen in der Tabelle.
5. Fehler → Tabelle zeigt Meldung.

## Nutzung

* Spring-Boot-Backend starten.
* Skript ausführen: `python app.py`
* Browser öffnen: `http://127.0.0.1:8050`
* Warehouse-ID eingeben → Daten werden angezeigt.
