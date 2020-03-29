package com.epam.brest.courses.service;

import com.epam.brest.courses.model.dto.CarDto;
import com.epam.brest.courses.service_api.CarDtoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-service.xml", "classpath:dao.xml"})
class CarDtoServiceImplIT {

    private final CarDtoService carDtoService;

    @Autowired
    CarDtoServiceImplIT(CarDtoService carDtoService) {
        this.carDtoService = carDtoService;
    }

    @Test
    void findAllWithNumberOfOrders() {
        //given
        String dateFrom = "2020-01-01";
        String dateTo = "2020-01-15";

        //when
        List<CarDto> list = carDtoService.findAllWithNumberOfOrders(dateFrom, dateTo);

        //then
        assertNotNull(list);
    }
}