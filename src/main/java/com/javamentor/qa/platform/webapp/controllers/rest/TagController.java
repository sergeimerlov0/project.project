package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user/tag/")
public class TagController {

    private final TagDtoService tagDtoService;

    @Autowired
    public TagController(TagDtoService tagDtoService) {
        this.tagDtoService = tagDtoService;
    }

    @GetMapping("tracked")
    public ResponseEntity<List<TagDto>> getAllTrackedTag(Principal user) {
        List<TagDto> tagDtos = tagDtoService.getTrackedTagByUsername(user.getName());
        return new ResponseEntity<>(tagDtoService.getTrackedTagByUsername(user.getName()), HttpStatus.OK);
    }

    @GetMapping("ignored")
    public ResponseEntity<List<TagDto>> getAllIgnoredTag(Principal user) {
        return new ResponseEntity<>(tagDtoService.getIgnoreTagByUsername(user.getName()), HttpStatus.OK);
    }
}
