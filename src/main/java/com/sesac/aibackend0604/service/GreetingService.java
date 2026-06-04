package com.sesac.aibackend0604.service;

import com.sesac.aibackend0604.util.MessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreetingService {

    private final MessageFormatter formatter;

    public String hello(String name) {
        return formatter.format(name);
    }
}
