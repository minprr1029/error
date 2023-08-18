package com.day24.preProject.questionComment.controller;

import com.day24.preProject.questionComment.dto.QuestionCommentPatchDto;
import com.day24.preProject.questionComment.dto.QuestionCommentPostDto;
import com.day24.preProject.questionComment.entity.QuestionComment;
import com.day24.preProject.questionComment.mapper.QuestionCommentMapper;
import com.day24.preProject.questionComment.service.QuestionCommentService;
import com.day24.preProject.dto.MultiResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Controller
@RequestMapping("/v11/QuestionComments")
@Validated
public class QuestionCommentController {
   private QuestionCommentService questionCommentService;
    private QuestionCommentMapper questionCommentMapper;

    public QuestionCommentController(QuestionCommentService questionCommentService, QuestionCommentMapper questionCommentMapper) {
        this.questionCommentService = questionCommentService;
        this.questionCommentMapper = questionCommentMapper;
    }

    @PostMapping
    public ResponseEntity postQuestionComment(@Valid @RequestBody QuestionCommentPostDto QuestionCommentPostDto) {
        QuestionComment QuestionComment = questionCommentService.createQustionComment(mapper.QuestionCommentPostDtoToQuestionComment(QuestionCommentPostDto));
        URI location = UriCreator.createUri(QuestionComment_DEFAULT_URL, QuestionComment.getQuestionCommentId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{QuestionComment-id}")
    public ResponseEntity patchQuestionComment(@PathVariable("QuestionComment-id") @Positive long QuestionCommentId,
                                      @Valid @RequestBody QuestionCommentPatchDto QuestionCommentPatchDto) {
        QuestionCommentPatchDto.setQuestionCommentId(QuestionCommentId);
        QuestionComment QuestionComment = QuestionCommentService.updateQuestionComment(mapper.QuestionCommentPatchDtoToQuestionComment(QuestionCommentPatchDto));

        return new ResponseEntity<>(
                SingleResponseDto.QuestionCommentToQuestionCommentResponseDto(questionComment),
                HttpStatus.OK);
    }

    @GetMapping("/{QuestionComment-id}")
    public ResponseEntity getQuestionComment(@PathVariable("QuestionComment-id") long QuestionCommentId) {
        QuestionComment QuestionComment = QuestionCommentService.findQuestionComment(QuestionCommentId);

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.QuestionCommentToQuestionCommentResponseDto(QuestionComment)),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getQuestionComments(@Positive @RequestParam int page,
                                     @Positive @RequestParam int size) {
        Page<QuestionComment> pageQuestionComments = QuestionCommentService.findQuestionComments(page - 1, size);
        List<QuestionComment> QuestionComments = pageQuestionComments.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.QuestionCommentsToQuestionCommentResponseDtos(QuestionComments),
                        pageQuestionComments),
                HttpStatus.OK);
    }

    @DeleteMapping("/{QuestionComment-id}")
    public ResponseEntity deleteQuestionComment(@PathVariable("QuestionComment-id") long QuestionCommentId) {
        QuestionCommentService.deleteQuestionComment(QuestionCommentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

