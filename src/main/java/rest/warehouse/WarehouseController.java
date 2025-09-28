package rest.warehouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rest.model.WarehouseData;

@RestController
public class WarehouseController {

    @Autowired
    private WarehouseService service;

    @RequestMapping("/")
    public String warehouseMain() {
        String mainPage = "This is the warehouse application! (DEZSYS_WAREHOUSE_REST) <br/><br/>" +
                "<a href='http://localhost:8080/warehouse/001/json'>Link to warehouse/001/json</a><br/>" +
                "<a href='http://localhost:8080/warehouse/001/xml'>Link to; warehouse/001/xml</a><br/>";
        return mainPage;
    }

    @GetMapping(
            value = "/warehouse/{inID}/json",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WarehouseData> warehouseData(@PathVariable String inID) {
        return ResponseEntity.ok(service.getWarehouseData(inID));
    }

    @GetMapping(
            value = "/warehouse/{inID}/xml",
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<WarehouseData> warehouseDataXml(@PathVariable String inID) {
        return ResponseEntity.ok(service.getWarehouseData(inID));
    }
}