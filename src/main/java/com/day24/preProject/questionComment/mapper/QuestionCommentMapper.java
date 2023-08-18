package com.day24.preProject.questionComment.mapper;

import com.day24.preProject.questionComment.dto.QuestionCommentPatchDto;
import com.day24.preProject.questionComment.dto.QuestionCommentPostDto;
import com.day24.preProject.questionComment.dto.QuestionCommentResponseDto;
import com.day24.preProject.questionComment.entity.QuestionComment;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface QuestionCommentMapper {

    QuestionComment questionReplyPostDtoToQuestionReply(QuestionCommentPostDto questionQuestionCommentPostDto);
    QuestionComment questionReplyPatchDtoToQuestionReply(QuestionCommentPatchDto questionPatchDto);
    QuestionCommentResponseDto questionReplyToQuestionReplyResponseDto(QuestionComment question);

    default List<QuestionCommentResponseDto> questionReplysToQuestionReplyResponseDtos(List<QuestionComment> questions){
        return questions.stream()
                .map(question -> QuestionCommentResponseDto.builder()
                        .questionId(question.getQuestionId())
                        .body(question.getBody())
                        .memberId(question.getMemberId())
                        .question_commentId(question.getQuestionCommentId())
                        .modifiedAt(question.getModifiedAt())
                        .createdAt(question.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
