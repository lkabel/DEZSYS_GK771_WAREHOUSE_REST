XML-Mapping mit Spring Boot und Jackson

1. Abhängigkeit

Zur Serialisierung von Java-Objekten nach XML wird die Bibliothek jackson-dataformat-xml benötigt.

Gradle (build.gradle.kts):

```kotlin
dependencies {  
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")  
}
```

2. REST-Controller

Ein Endpunkt wird so konfiguriert, dass er den MIME-Typ application/xml produziert.

```java
@GetMapping(
    value = "/warehouse/{inID}/xml",
    produces = MediaType.APPLICATION_XML_VALUE
)
public ResponseEntity<WarehouseData> warehouseDataXml(@PathVariable String inID){
    return ResponseEntity.ok(service.getWarehouseData(inID));
}
```

3. Datenmodell und Annotationen

Die Java-Klasse wird mit Jackson-Annotationen annotiert, um die XML-Struktur zu definieren.

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

Annotationen:

* @JacksonXmlRootElement(localName = "warehouseData"): Definiert das XML-Root Element als <warehouseData>.
* @JacksonXmlProperty(localName = "warehouseID"): Benennt das XML-Element für ein einzelnes Feld, z.B. <warehouseID>.
* @JacksonXmlElementWrapper(localName = "productData"): Erzeugt ein umschließendes Element (<productData>) für eine Collection (Array/Liste).
* @JacksonXmlProperty(localName = "product"): Benennt die einzelnen Elemente innerhalb der umschlossenen Collection (<product>).

4. Funktionsweise in Spring

* HttpMessageConverter: Spring Boot konfiguriert automatisch den MappingJackson2XmlHttpMessageConverter, wenn jackson-dataformat-xml im Classpath vorhanden ist.
* Content-Type: Anhand der produces-Angabe im Controller (application/xml) wählt Spring diesen Converter.
* XmlMapper: Der Converter nutzt intern Jacksons XmlMapper, um das Java-Objekt anhand der Annotationen in einen XML-String zu serialisieren.

5. Datenerzeugung

```java
products[3] = new Product();
products[3].setProductID("00-316253");
products[3].setProductName("Persil Discs Color");
products[3].setProductCategory("Waschmittel");
products[3].setProductQuantity(1430);
products[3].setProductUnit("Packung 700G");
```

Dash-Client für Warehouse-Daten

1. Layout mit Eingabefeld und Tabelle:

```python
dcc.Input(id="warehouse-id", type="text", value="001"),
dash_table.DataTable(id="table")
```

2. Callback holt Daten von [http://localhost:8080/warehouse/{id}/json](http://localhost:8080/warehouse/{id}/json):

```python
data = requests.get(url).json()
products = data["productData"]
```

3. Produkte werden ins DataFrame geladen und um Lagerinfos ergänzt:

```python
df = pd.DataFrame(products)
for k, v in warehouse_info.items():
    df[k] = v
```

4. DataFrame wird in Spalten und Zeilen für die Tabelle umgewandelt:

```python
columns = [{"name": i, "id": i} for i in df.columns]
records = df.to_dict("records")
```

Erkentnisse
* Spring kann automatisch basierend auf dem Content-Type XML oder JSON ausgeben
* XML-Struktur wird durch annotationen definiert.
