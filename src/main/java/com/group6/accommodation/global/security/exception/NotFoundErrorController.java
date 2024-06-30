package com.group6.accommodation.global.security.exception;

import com.group6.accommodation.global.security.exception.dto.SecurityExceptionDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NotFoundErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> handleNotFound(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        int resultCode = Integer.parseInt(status.toString());
        String message = Objects.requireNonNull(HttpStatus.resolve(resultCode)).getReasonPhrase();

        Map<String, String> data = new HashMap<>();
        data.put("message", message);

        SecurityExceptionDto<Object> response = SecurityExceptionDto.builder()
                .resultCode(resultCode)
                .resultMessage(message)
                .data(data)
                .build();

        return ResponseEntity.status(HttpStatus.valueOf(resultCode)).body(response);
    }
}
