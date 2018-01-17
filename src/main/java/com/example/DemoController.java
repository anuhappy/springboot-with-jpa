package com.helloworld.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class DemoController {

private final JdbcTemplate jdbcTemplate;
@Autowired
 public DemoController(JdbcTemplate jdbcTemplate) {


    this.jdbcTemplate = jdbcTemplate;

}
 @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String index() {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8085/getCountry";
        ResponseEntity<String> response
                = restTemplate.getForEntity(fooResourceUrl,String.class);
        org.springframework.boot.json.JsonParser springParser = JsonParserFactory.getJsonParser();
        List<Object> result = springParser.parseList(response.getBody());
        try {
            FileOutputStream f = new FileOutputStream(new File("/Users/anuragkumardwivedi/Desktop/test/test.sql"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(f));
            for (Object data : result) {
                     bw.write("insert into country values(\""+((Map)data).values().toArray()[0]+"\",\""+((Map)data).values().toArray()[1]+"\");");
                     bw.newLine();
                     
            }
            bw.flush();
            bw.close();
            f.close();


            jdbcTemplate.execute("test.sql");
        }


        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return response.toString();
    }
}