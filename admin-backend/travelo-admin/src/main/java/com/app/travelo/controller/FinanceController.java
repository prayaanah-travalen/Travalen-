package com.app.travelo.controller;

import com.app.travelo.model.rest.FinanceDto;
import com.app.travelo.model.rest.ResponseDto;
import com.app.travelo.services.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/finance")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDto<FinanceDto>> saveFinance(@RequestBody FinanceDto req) {
       return  new ResponseEntity(financeService.saveFinance(req), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto<FinanceDto>> getFinance(@RequestBody FinanceDto req) {
        return  new ResponseEntity(financeService.getFinance(req), HttpStatus.OK);
    }
}
