package com.iia.store.controller.member;

import com.iia.store.config.response.Response;
import com.iia.store.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/{id}")
    public ResponseEntity<Response> read(@PathVariable(name = "id")Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(memberService.read(id)));
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Response> delete(@PathVariable(name = "id")Long id) {
        memberService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success());
    }
}