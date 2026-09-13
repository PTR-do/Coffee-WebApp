package capp.modules.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager/access")
public class ManagerAccessController {

    @GetMapping()
    public String manager() {
        return "Manager/manager_home";
    }

    @GetMapping("/handleMaintenance")
    public String handleMaintenance() {
        return "Manager/manager_maintenance";
    }

    @GetMapping("/handleDistributor")
    public String handleDistributor() {
        return "Manager/manager_distributor";
    }

    @GetMapping("/stateDistributor")
    private String stateDistributor() {
        return "Manager/manager_distributorState";
    }

    @GetMapping("/map")
    public String managerMap() {
        return "Manager/manager_map";
    }

    @GetMapping("/return")
    public String returnHome() {
        return "Manager/manager_home";
    }
}