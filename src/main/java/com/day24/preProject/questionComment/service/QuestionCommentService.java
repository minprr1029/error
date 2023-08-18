package com.day24.preProject.questionComment.service;

import com.day24.preProject.question.repository.AnswerRepository;
import com.day24.preProject.question.repository.QuestionRepository;
import com.day24.preProject.questionComment.entity.QuestionComment;
import com.day24.preProject.questionComment.repository.QuestionCommentRepository;
import com.day24.preProject.exception.BusinessLogicException;
import com.day24.preProject.exception.ExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionCommentService {
    private final com.day24.preProject.questionComment.repository.QuestionCommentRepository QuestionCommentRepository;

    private final QuestionRepository questionRepository;

    public QuestionCommentService(QuestionCommentRepository QuestionCommentRepository) {
        this.QuestionCommentRepository = QuestionCommentRepository;
    }

    public QuestionComment createQuestionComment(QuestionComment QuestionComment) {

        String QuestionCommentCode = QuestionComment.getQuestionCommentCode().toUpperCase();


        verifyExistQuestionComment(QuestionCommentCode);
        QuestionComment.setQuestionCommentCode(QuestionCommentCode);

        return QuestionCommentRepository.save(QuestionComment);
    }

    public QuestionComment updateQuestionComment(QuestionComment QuestionComment) {

        QuestionComment findQuestionComment = findVerifiedQuestionComment(QuestionComment.getQuestionCommentId());

        Optional.ofNullable(QuestionComment.getKorName())
                .ifPresent(korName -> findQuestionComment.setKorName(korName));
        Optional.ofNullable(QuestionComment.getEngName())
                .ifPresent(engName -> findQuestionComment.setEngName(engName));
        Optional.ofNullable(QuestionComment.getPrice())
                .ifPresent(price -> findQuestionComment.setPrice(price));

        Optional.ofNullable(QuestionComment.getQuestionCommentStatus())
                .ifPresent(QuestionCommentStatus -> findQuestionComment.setQuestionCommentStatus(QuestionCommentStatus));

        return QuestionCommentRepository.save(findQuestionComment);
    }

    public QuestionComment findQuestionComment(long QuestionCommentId) {
        return findVerifiedQuestionCommentByQuery(QuestionCommentId);
    }

    public Page<QuestionComment> findQuestionComments(int page, int size) {
        return QuestionCommentRepository.findAll(PageRequest.of(page, size,
                Sort.by("QuestionCommentId").descending()));
    }

    public void deleteQuestionComment(long QuestionCommentId) {
        QuestionComment QuestionComment = findVerifiedQuestionComment(QuestionCommentId);
        QuestionCommentRepository.delete(QuestionComment);
    }

    public QuestionComment findVerifiedQuestionComment(long QuestionCommentId) {
        Optional<QuestionComment> optionalQuestionComment = QuestionCommentRepository.findById(QuestionCommentId);
        QuestionComment findQuestionComment =
                optionalQuestionComment.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.QuestionComment_NOT_FOUND));

        return findQuestionComment;
    }

    private void verifyExistQuestionComment(String QuestionCommentCode) {
        Optional<QuestionComment> QuestionComment = QuestionCommentRepository.findByQuestionCommentCode(QuestionCommentCode);
        if(QuestionComment.isPresent())
            throw new BusinessLogicException(ExceptionCode.QuestionComment_CODE_EXISTS);
    }

    private QuestionComment findVerifiedQuestionCommentByQuery(long QuestionCommentId) {
        Optional<QuestionComment> optionalQuestionComment = QuestionCommentRepository.findByQuestionComment(QuestionCommentId);
        QuestionComment findQuestionComment =
                optionalQuestionComment.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.QuestionComment_NOT_FOUND));

        return findQuestionComment;
    }
}
