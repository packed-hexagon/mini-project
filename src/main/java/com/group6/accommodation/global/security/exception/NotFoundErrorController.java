package com.group6.accommodation.global.security.exception;

import com.group6.accommodation.global.security.exception.dto.SecurityExceptionDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
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

            Map<String, String> message = new HashMap<>();
            message.put("message", "요청하신 페이지는 찾을 수 없는 페이지입니다.");

            int resultCode = Integer.parseInt(status.toString());

            SecurityExceptionDto response = SecurityExceptionDto.builder()
                    .resultCode(resultCode)
                    .resultMessage(HttpStatus.resolve(resultCode).getReasonPhrase())
                    .data(message)
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
