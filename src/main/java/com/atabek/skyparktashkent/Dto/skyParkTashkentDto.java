package com.atabek.skyparktashkent.Dto;

import com.atabek.skyparktashkent.model.SkyParkModel.AboutUs;
import com.atabek.skyparktashkent.model.SkyParkModel.Counter;
import lombok.Getter;

import java.util.List;

@Getter
public class skyParkTashkentDto {
    private Counter counter;
    private AboutUs aboutUs;
    private List<String> principleContentList;
}
