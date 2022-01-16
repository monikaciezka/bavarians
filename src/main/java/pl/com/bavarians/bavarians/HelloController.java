package pl.com.bavarians.bavarians;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {
    @RequestMapping("/hi")
    public ResponseEntity<String> hello() {
        return ResponseEntity.status(HttpStatus.OK).body("<body> adixxx </body>");
    }
}
