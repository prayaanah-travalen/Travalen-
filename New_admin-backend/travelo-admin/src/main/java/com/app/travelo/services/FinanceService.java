package com.app.travelo.services;

import com.app.travelo.model.rest.FinanceDto;
import com.app.travelo.model.rest.ResponseDto;

public interface FinanceService {
    ResponseDto<FinanceDto> saveFinance(FinanceDto req);
    ResponseDto<FinanceDto> getFinance(FinanceDto req);
}
