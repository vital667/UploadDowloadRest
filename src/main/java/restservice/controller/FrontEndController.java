package restservice.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontEndController {


    @ApiOperation(value = "Web Interface for loading File/Multiple Files and download them",
            notes = "Web Interface for loading File/Multiple Files and download them")
    @GetMapping
    public String index(){
        return "index";
    }


    @ApiOperation(value = "Web Interface for loading files for 'ReadAndCount' and 'ReplaceEverySecond' and download results",
            notes = "Web Interface for loading files for 'ReadAndCount' and 'ReplaceEverySecond' and download results")
    @GetMapping("/home")
    public String home(){
        return "home";
    }
}
