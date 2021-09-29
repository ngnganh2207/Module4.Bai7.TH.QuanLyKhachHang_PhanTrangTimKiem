package com.codegym.formatter;

import com.codegym.model.Province;
import com.codegym.service.impl.IProvinceService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

@Component
public class ProvinceFomatter implements Formatter<Province> {

    private IProvinceService provinceService;

    public ProvinceFomatter() {
    }

    public ProvinceFomatter(IProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    @Override
    public Province parse(String text, Locale locale) throws ParseException {
        Optional<Province> province= provinceService.findById(Long.parseLong(text));
        return province.orElse(null);
    }

    @Override
    public String print(Province object, Locale locale) {
        return null;
    }
}
