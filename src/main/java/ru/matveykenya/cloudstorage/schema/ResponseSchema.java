package ru.matveykenya.cloudstorage.schema;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseSchema {

    static public ResponseEntity<Object> error(int id, String massege){
        Map<Object, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("message", massege);
        return ResponseEntity.status(id).body(response);
    }

    static public ResponseEntity<Object> login(String token){
        Map<Object, Object> response = new HashMap<>();
        response.put("auth-token", token);
        return ResponseEntity.ok(response);
    }

    /**
     * success operation
     * @return status 200
     */
    static public ResponseEntity<Object> ok(){
        Map<Object, Object> response = new HashMap<>();
        response.put("description", "Success operation");
        return ResponseEntity.ok(response);
    }

    /**
     * error input data
     * @return status 400
     */
    static public ResponseEntity<Object> error400(){
        return error(400, "error input data");
    }

    /**
     * error during current operation
     * @return status 500
     */
    static public ResponseEntity<Object> error500(){
        return error(500, "error during operation");
    }
}
