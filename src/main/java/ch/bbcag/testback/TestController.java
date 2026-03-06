package ch.bbcag.testback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/random")
public class TestController {
  @CrossOrigin(origins = "*")
  @GetMapping("/num")
  public ResponseEntity<Integer> getNum() {
    return new ResponseEntity<>(67, HttpStatus.OK);
  }
}
