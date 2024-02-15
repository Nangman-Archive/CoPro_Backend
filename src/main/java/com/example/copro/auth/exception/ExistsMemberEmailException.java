package com.example.copro.auth.exception;

import com.example.copro.global.error.exception.InvalidGroupException;

public class ExistsMemberEmailException extends InvalidGroupException {
    public ExistsMemberEmailException(String message) {
        super(message);
    }

    public ExistsMemberEmailException() {
        this("해당 이메일로 이미 가입한 계정이 있습니다. 다른 소셜 플랫폼에서 가입하였다면, 해당 플랫폼을 통해 로그인을 시도해주십시오.");
    }
}
