package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.dto.AnswerBodyDto;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.entity.BookMarks;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestQuestionResourceController extends AbstractApiTest {
    @Autowired
    private TagService tagService;

    @Autowired
    private QuestionService questionService;

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/postQuestionView/answer.yml",
            "datasets/QuestionResourceController/postQuestionView/comment.yml",
            "datasets/QuestionResourceController/postQuestionView/commentQuestion.yml",
            "datasets/QuestionResourceController/postQuestionView/question.yml",
            "datasets/QuestionResourceController/postQuestionView/questionHasTag.yml",
            "datasets/QuestionResourceController/postQuestionView/reputation.yml",
            "datasets/QuestionResourceController/postQuestionView/role.yml",
            "datasets/QuestionResourceController/postQuestionView/tag.yml",
            "datasets/QuestionResourceController/postQuestionView/user.yml",
            "datasets/QuestionResourceController/postQuestionView/voteQuestion.yml"
    }, cleanBefore = true, cleanAfter = true)
    void postQuestionView() throws Exception {
        //???????????????? ???? ???????????????????????????? ????????????
        this.mvc.perform(post("/api/user/question/1/view")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().is4xxClientError());

        // ???????????????? ???? ???????????????????? ??????????????????
        this.mvc.perform(post("/api/user/question/100/view")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

        QuestionViewed questionViewed = em.createQuery(
                        "SELECT a FROM QuestionViewed a " +
                                "WHERE a.question.id = :questionId " +
                                "AND a.user.email = :useremail",
                        QuestionViewed.class)
                .setParameter("questionId", 100L)
                .setParameter("useremail", "3user@mail.ru")
                .getSingleResult();
        Assertions.assertNotNull(questionViewed);

        // ???????????????? ???????????????????????? ??????????????????
        this.mvc.perform(post("/api/user/question/100/view")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());
        List<QuestionViewed> questionViewed2 = em.createQuery(
                        "SELECT a FROM QuestionViewed a " +
                                "WHERE a.question.id = :questionId " +
                                "AND a.user.email = :useremail",
                        QuestionViewed.class)
                .setParameter("questionId", 100L)
                .setParameter("useremail", "3user@mail.ru")
                .getResultList();
        assertEquals(1, questionViewed2.size());
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/getQuestionViewDtoById/answer.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoById/comment.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoById/commentQuestion.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoById/question.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoById/questionHasTag.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoById/reputation.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoById/role.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoById/tag.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoById/user.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoById/voteAnswer.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoById/voteQuestion.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getQuestionViewDtoById() throws Exception {
        this.mvc.perform(get("/api/user/question/100")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.title").value("title by question 100"))
                .andExpect(jsonPath("$.countAnswer").value(3L))
                .andExpect(jsonPath("$.authorReputation").value(6L))
                .andExpect(jsonPath("$.countValuable").value(2L))
                .andDo(print())

                .andExpect(jsonPath("$.isUserAnswerVote").value(true))

                //??????????????????, ?????? ?? ?????????????? ?? id=100 ???????????????????????? ?????????????????????? (2 ????.)
                .andExpect(jsonPath("$.comments.length()").value(2L))
                .andExpect(jsonPath("$.comments.[0].id").value(100L))
                .andExpect(jsonPath("$.comments.[1].id").value(101L));

        this.mvc.perform(get("/api/user/question/103")
                .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(103L))
                .andExpect(jsonPath("$.isUserAnswerVote").value(false))
                .andDo(print());

    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/getNonExistedQuestionViewDtoById/answer.yml",
            "datasets/QuestionResourceController/getNonExistedQuestionViewDtoById/comment.yml",
            "datasets/QuestionResourceController/getNonExistedQuestionViewDtoById/commentQuestion.yml",
            "datasets/QuestionResourceController/getNonExistedQuestionViewDtoById/question.yml",
            "datasets/QuestionResourceController/getNonExistedQuestionViewDtoById/questionHasTag.yml",
            "datasets/QuestionResourceController/getNonExistedQuestionViewDtoById/reputation.yml",
            "datasets/QuestionResourceController/getNonExistedQuestionViewDtoById/role.yml",
            "datasets/QuestionResourceController/getNonExistedQuestionViewDtoById/tag.yml",
            "datasets/QuestionResourceController/getNonExistedQuestionViewDtoById/user.yml",
            "datasets/QuestionResourceController/getNonExistedQuestionViewDtoById/voteQuestion.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getNonExistedQuestionViewDtoById() throws Exception {
        this.mvc.perform(get("/api/user/question/200")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/postUpVoteQuestion/answer.yml",
            "datasets/QuestionResourceController/postUpVoteQuestion/comment.yml",
            "datasets/QuestionResourceController/postUpVoteQuestion/commentQuestion.yml",
            "datasets/QuestionResourceController/postUpVoteQuestion/question.yml",
            "datasets/QuestionResourceController/postUpVoteQuestion/questionHasTag.yml",
            "datasets/QuestionResourceController/postUpVoteQuestion/reputation.yml",
            "datasets/QuestionResourceController/postUpVoteQuestion/role.yml",
            "datasets/QuestionResourceController/postUpVoteQuestion/tag.yml",
            "datasets/QuestionResourceController/postUpVoteQuestion/user.yml",
            "datasets/QuestionResourceController/postUpVoteQuestion/voteQuestion.yml"
    }, cleanBefore = true, cleanAfter = true)
        //??????????????????, ?????? ?????????????????????? ???? ???????????? ???????????????????? ???????????????????? ??????????????. ???????????? ???????? 1 ??????????.
    void postUpVoteQuestion() throws Exception {
        this.mvc.perform(post("/api/user/question/103/upVote")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));


        //??????????????????, ?????? ?? ?????????????? VoteQuestions ???????????????? ??????????
        VoteQuestion vq = em.createQuery(
                        "SELECT a FROM VoteQuestion a " +
                                "WHERE a.question.id = :questionId " +
                                "AND a.user.id = :userId",
                        VoteQuestion.class)
                .setParameter("questionId", 103L)
                .setParameter("userId", 100L)
                .getSingleResult();
        Assertions.assertNotNull(vq);
        Assertions.assertEquals(VoteType.UP_VOTE, vq.getVote());


        //??????????????????, ?????? ???????????????????? ?????????????????????????? ???? ???????? ????????????
        Assertions.assertTrue(this.mvc.perform(post("/api/user/question/103/upVote")
                        .header("Authorization", getJwtToken("test3@test.ru", "123")))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Voting for your question with id " + 103 + " not allowed"));

        //??????????????????, ?????? ???????????????????? ?????????????????????????? ???? ???????????? ????????????
        Assertions.assertTrue(this.mvc.perform(post("/api/user/question/103/upVote")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("You allready voted for the question with id " + 103));


        //??????????????????, ?????? ???????????????????? ?????????????????????????? ???? ????????????, ???????????????? ??????.
        Assertions.assertTrue(this.mvc.perform(post("/api/user/question/104/upVote")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Question with id " + 104 + " not found"));

    }




    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/postDownVoteQuestion/answer.yml",
            "datasets/QuestionResourceController/postDownVoteQuestion/comment.yml",
            "datasets/QuestionResourceController/postDownVoteQuestion/commentQuestion.yml",
            "datasets/QuestionResourceController/postDownVoteQuestion/question.yml",
            "datasets/QuestionResourceController/postDownVoteQuestion/questionHasTag.yml",
            "datasets/QuestionResourceController/postDownVoteQuestion/reputation.yml",
            "datasets/QuestionResourceController/postDownVoteQuestion/role.yml",
            "datasets/QuestionResourceController/postDownVoteQuestion/tag.yml",
            "datasets/QuestionResourceController/postDownVoteQuestion/user.yml",
            "datasets/QuestionResourceController/postDownVoteQuestion/voteQuestion.yml"
    }, cleanBefore = true, cleanAfter = true)

        //??????????????????, ?????? ?????????????????????? ???? ???????????? ???????????????????? ???????????????????? ??????????????. ???????????? ???????? -1
    void postDownVoteQuestion() throws Exception {
        this.mvc.perform(post("/api/user/question/102/downVote")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(content().string("-1"));


        //??????????????????, ?????? ?? ???? ???????????????? ??????????
        VoteQuestion vq = em.createQuery(
                        "SELECT a FROM VoteQuestion a " +
                                "WHERE a.question.id = :questionId " +
                                "AND a.user.id = :userId",
                        VoteQuestion.class)
                .setParameter("questionId", 102L)
                .setParameter("userId", 100L)
                .getSingleResult();
        Assertions.assertNotNull(vq);
        Assertions.assertEquals(VoteType.DOWN_VOTE, vq.getVote());


        //??????????????????, ?????? ???????????????????? ?????????????????????????? ???? ???????? ????????????
        Assertions.assertTrue(this.mvc.perform(post("/api/user/question/103/downVote")
                        .header("Authorization", getJwtToken("test3@test.ru", "123")))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Voting for your question with id " + 103 + " not allowed"));

        //??????????????????, ?????? ???????????????????? ?????????????????????????? ???? ???????????? ????????????
        Assertions.assertTrue(this.mvc.perform(post("/api/user/question/102/downVote")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("You allready voted for the question with id " + 102));

        //??????????????????, ?????? ???????????????????? ?????????????????????????? ???? ????????????, ???????????????? ??????.
        Assertions.assertTrue(this.mvc.perform(post("/api/user/question/104/downVote")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Question with id " + 104 + " not found"));


    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/postUpVoteQuestionByNullQuestion/answer.yml",
            "datasets/QuestionResourceController/postUpVoteQuestionByNullQuestion/comment.yml",
            "datasets/QuestionResourceController/postUpVoteQuestionByNullQuestion/commentQuestion.yml",
            "datasets/QuestionResourceController/postUpVoteQuestionByNullQuestion/question.yml",
            "datasets/QuestionResourceController/postUpVoteQuestionByNullQuestion/questionHasTag.yml",
            "datasets/QuestionResourceController/postUpVoteQuestionByNullQuestion/reputation.yml",
            "datasets/QuestionResourceController/postUpVoteQuestionByNullQuestion/role.yml",
            "datasets/QuestionResourceController/postUpVoteQuestionByNullQuestion/tag.yml",
            "datasets/QuestionResourceController/postUpVoteQuestionByNullQuestion/user.yml",
            "datasets/QuestionResourceController/postUpVoteQuestionByNullQuestion/voteQuestion.yml"
    }, cleanBefore = true, cleanAfter = true)
    void postUpVoteQuestionByNullQuestion() throws Exception {
        this.mvc.perform(post("/api/user/question/200/upVote")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/postDownVoteQuestionByNullQuestion/answer.yml",
            "datasets/QuestionResourceController/postDownVoteQuestionByNullQuestion/comment.yml",
            "datasets/QuestionResourceController/postDownVoteQuestionByNullQuestion/commentQuestion.yml",
            "datasets/QuestionResourceController/postDownVoteQuestionByNullQuestion/question.yml",
            "datasets/QuestionResourceController/postDownVoteQuestionByNullQuestion/questionHasTag.yml",
            "datasets/QuestionResourceController/postDownVoteQuestionByNullQuestion/reputation.yml",
            "datasets/QuestionResourceController/postDownVoteQuestionByNullQuestion/role.yml",
            "datasets/QuestionResourceController/postDownVoteQuestionByNullQuestion/tag.yml",
            "datasets/QuestionResourceController/postDownVoteQuestionByNullQuestion/user.yml",
            "datasets/QuestionResourceController/postDownVoteQuestionByNullQuestion/voteQuestion.yml"
    }, cleanBefore = true, cleanAfter = true)
    void postDownVoteQuestionByNullQuestion() throws Exception {
        this.mvc.perform(post("/api/user/question/200/downVote")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/getAllQuestionCommentByQuestionId/answer.yml",
            "datasets/QuestionResourceController/getAllQuestionCommentByQuestionId/comment.yml",
            "datasets/QuestionResourceController/getAllQuestionCommentByQuestionId/commentQuestion.yml",
            "datasets/QuestionResourceController/getAllQuestionCommentByQuestionId/question.yml",
            "datasets/QuestionResourceController/getAllQuestionCommentByQuestionId/questionHasTag.yml",
            "datasets/QuestionResourceController/getAllQuestionCommentByQuestionId/reputation.yml",
            "datasets/QuestionResourceController/getAllQuestionCommentByQuestionId/role.yml",
            "datasets/QuestionResourceController/getAllQuestionCommentByQuestionId/tag.yml",
            "datasets/QuestionResourceController/getAllQuestionCommentByQuestionId/user.yml",
            "datasets/QuestionResourceController/getAllQuestionCommentByQuestionId/voteQuestion.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void getAllQuestionCommentByQuestionId() throws Exception {
        this.mvc.perform(get("/api/user/question/100/comment")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].questionId", is(100)))
                .andExpect(jsonPath("$[0].text", is("some text 1")))
                .andExpect(jsonPath("$[0].userId", is(100)))
                .andExpect(jsonPath("$[0].reputation", is(6)))
                .andExpect(jsonPath("$[1].id", is(101)))
                .andExpect(jsonPath("$[1].questionId", is(100)))
                .andExpect(jsonPath("$[1].text", is("some text 2")))
                .andExpect(jsonPath("$[1].userId", is(101)));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/getEmptyListQuestionCommentByQuestionId/answer.yml",
            "datasets/QuestionResourceController/getEmptyListQuestionCommentByQuestionId/comment.yml",
            "datasets/QuestionResourceController/getEmptyListQuestionCommentByQuestionId/commentQuestion.yml",
            "datasets/QuestionResourceController/getEmptyListQuestionCommentByQuestionId/question.yml",
            "datasets/QuestionResourceController/getEmptyListQuestionCommentByQuestionId/questionHasTag.yml",
            "datasets/QuestionResourceController/getEmptyListQuestionCommentByQuestionId/reputation.yml",
            "datasets/QuestionResourceController/getEmptyListQuestionCommentByQuestionId/role.yml",
            "datasets/QuestionResourceController/getEmptyListQuestionCommentByQuestionId/tag.yml",
            "datasets/QuestionResourceController/getEmptyListQuestionCommentByQuestionId/user.yml",
            "datasets/QuestionResourceController/getEmptyListQuestionCommentByQuestionId/voteQuestion.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void getEmptyListQuestionCommentByQuestionId() throws Exception {
        this.mvc.perform(get("/api/user/question/101/comment")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/shouldNotGetQuestionCommentByQuestionId/answer.yml",
            "datasets/QuestionResourceController/shouldNotGetQuestionCommentByQuestionId/comment.yml",
            "datasets/QuestionResourceController/shouldNotGetQuestionCommentByQuestionId/commentQuestion.yml",
            "datasets/QuestionResourceController/shouldNotGetQuestionCommentByQuestionId/question.yml",
            "datasets/QuestionResourceController/shouldNotGetQuestionCommentByQuestionId/questionHasTag.yml",
            "datasets/QuestionResourceController/shouldNotGetQuestionCommentByQuestionId/reputation.yml",
            "datasets/QuestionResourceController/shouldNotGetQuestionCommentByQuestionId/role.yml",
            "datasets/QuestionResourceController/shouldNotGetQuestionCommentByQuestionId/tag.yml",
            "datasets/QuestionResourceController/shouldNotGetQuestionCommentByQuestionId/user.yml",
            "datasets/QuestionResourceController/shouldNotGetQuestionCommentByQuestionId/voteQuestion.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void shouldNotGetQuestionCommentByQuestionId() throws Exception {
        this.mvc.perform(get("/api/user/question/500/comment")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Question with id 500 not found!"));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/getQuestionViewDtoByTagId/answer.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoByTagId/comment.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoByTagId/commentQuestion.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoByTagId/question.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoByTagId/questionHasTag.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoByTagId/reputation.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoByTagId/role.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoByTagId/tag.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoByTagId/user.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoByTagId/voteQuestion.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDto/bookmarks.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getQuestionViewDtoByTagId() throws Exception {
        //?????????????????? ???????????????????????? Response. ?? ?????????????????? 3 ?????????????? c id 100, 101, 102, ?????????????? ?????????? ?? TagId 100,
        //???? ???????????? c id 102 ?????????? ???????? IsDeleted=true
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/tag/100?page=1")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2))
                .andExpect(jsonPath("$.itemsOnPage").value(10))

                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionViewDto
                .andExpect(jsonPath("$.items.[0].id").value(100))
                .andExpect(jsonPath("$.items.[1].id").value(101))

                //??????????????????, ?????? ???????????????? ?????????? QuestionViewDto, ????????????????, ?? id 100 ??????????????????
                .andExpect(jsonPath("$.items.[0].title").value("title by question 100"))
                .andExpect(jsonPath("$.items.[0].authorId").value(100))
                .andExpect(jsonPath("$.items.[0].authorReputation").value(6))
                .andExpect(jsonPath("$.items.[0].authorName").value("User with id 100"))
                .andExpect(jsonPath("$.items.[0].authorImage").value("image100"))
                .andExpect(jsonPath("$.items.[0].description").value("description by question 100"))
                .andExpect(jsonPath("$.items.[0].viewCount").value(0))
                .andExpect(jsonPath("$.items.[0].countAnswer").value(3))
                .andExpect(jsonPath("$.items.[0].countValuable").value(2))
                .andExpect(jsonPath("$.items.[0].isUserBookmark").value(true))

                //??????????????????, ?????? ???????????? QuestionViewDto ?????????? ?????????????????? ???????????? ???????? tags, ???????????????????? ?? ????????
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].name").value("test tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].description").value("description for tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[1].id").value(101))
                .andExpect(jsonPath("$.items.[0].listTagDto.[2].id").value(102));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/getQuestionViewDtoNoAnswer/answer.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoNoAnswer/question.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoNoAnswer/questionHasTag.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoNoAnswer/role.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoNoAnswer/tag.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoNoAnswer/voteQuestion.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoNoAnswer/user.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoNoAnswer/reputation.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoNoAnswer/comment.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoNoAnswer/commentQuestion.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoNoAnswer/bookmarks.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getQuestionViewDtoNoAnswer() throws Exception {
        //?? ?????????????????? 4 ?????????????? c id 100, 102, 103 ?? 104 ???? ?????????????? ?????? ????????????,
        // ???? ???????????? c id 102 ?????????? ???????? IsDeleted=true
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/noAnswer?page=1")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(3))
                .andExpect(jsonPath("$.itemsOnPage").value(10))

                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionViewDto
                .andExpect(jsonPath("$.items.[0].id").value(100))
                .andExpect(jsonPath("$.items.[1].id").value(103))
                .andExpect(jsonPath("$.items.[2].id").value(104))

                //??????????????????, ?????? ???????????????? ?????????? QuestionViewDto, ????????????????, ?? id 100 ??????????????????
                .andExpect(jsonPath("$.items.[0].title").value("title by question 100"))
                .andExpect(jsonPath("$.items.[0].authorId").value(100))
                .andExpect(jsonPath("$.items.[0].authorName").value("User with id 100"))
                .andExpect(jsonPath("$.items.[0].authorImage").value("image100"))
                .andExpect(jsonPath("$.items.[0].description").value("description by question 100"))
                .andExpect(jsonPath("$.items.[0].viewCount").value(0))
                .andExpect(jsonPath("$.items.[0].countAnswer").value(0))
                .andExpect(jsonPath("$.items.[0].countValuable").value(1))
                .andExpect(jsonPath("$.items.[0].isUserBookmark").value(true))

                //??????????????????, ?????? ???????????? QuestionDto ?????????? ?????????????????? ???????????? ???????? tags, ?????????????????? ?? ??????
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].name").value("test tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[1].id").value(101));

        //??????????????????, ?????? ???? trackedTags ???????????????? ???????????? question ?? id 100
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/noAnswer?page=1&trackedTags=100")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.itemsOnPage").value(10))

                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionViewDto
                .andExpect(jsonPath("$.items.[0].id").value(100));

        //??????????????????, ?????? ???? ignoredTags ???????????????? ???????????? question ?? id 103 ?? 104
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/noAnswer?page=1&ignoredTags=100")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2))
                .andExpect(jsonPath("$.itemsOnPage").value(10))

                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionViewDto
                .andExpect(jsonPath("$.items.[0].id").value(103))
                .andExpect(jsonPath("$.items.[1].id").value(104));

        //??????????????????, ?????? ???? trackedTags ?? ???? ignoredTags ???????????????? ???????????? question ?? id 100 ?? 104
        this.mvc.perform(MockMvcRequestBuilders
                        .get("/api/user/question/noAnswer?page=1&trackedTags=100,103&ignoredTags=102")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2))
                .andExpect(jsonPath("$.itemsOnPage").value(10))

                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionDto
                .andExpect(jsonPath("$.items.[0].id").value(100))
                .andExpect(jsonPath("$.items.[1].id").value(104));

        //???????????????? ??????????????, ???? ?????????????? items ???? ????????????????????, ?? ???????????? ???????????? ???? trackedTags
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/noAnswer?page=1&trackedTags=104")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(0))
                .andExpect(jsonPath("$.totalResultCount").value(0))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/getAllQuestionViewDto/answer.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDto/question.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDto/questionHasTag.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDto/reputation.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDto/role.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDto/tag.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDto/user.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDto/voteQuestion.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDto/comment.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDto/commentQuestion.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDto/bookmarks.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getAllQuestionViewDto() throws Exception {
        // ?????????????????? ???????????????????????? Response.
        // ?? ?????????????????? 5 ???????????????? c id 100-105,
        // TagId = 100 ?????????????????? ?? ???????????????? 100, 101, 102
        // TagId = 103 ?????????????????? ?? ???????????????? 104, 105
        // ?????????????? ?????? 101, ?????????????? ?????????????? ?? ???????????????? 100 ?? 103
        // ???????????? c id 102 ?????????? ???????? isDeleted = true
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/?currentPageNumber=1&itemsOnPage=2&trackedTags=100,103&ignoredTags=101")
                        .header("Authorization", getJwtToken("test_user100@mail.ru", "123")))
                .andExpect(status().isOk())
                .andDo(print())
                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(2))
                .andExpect(jsonPath("$.totalResultCount").value(3))
                .andExpect(jsonPath("$.itemsOnPage").value(2))

                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionViewDto
                .andExpect(jsonPath("$.items.[0].id").value(101))
                .andExpect(jsonPath("$.items.[1].id").value(104))

                //??????????????????, ?????? ???????????????? ?????????? QuestionViewDto, ????????????????, ?? id 101 ??????????????????
                .andExpect(jsonPath("$.items.[0].title").value("test title by question 101"))
                .andDo(print())
                .andExpect(jsonPath("$.items.[0].authorId").value(101))
                .andExpect(jsonPath("$.items.[0].authorReputation").value(2))
                .andExpect(jsonPath("$.items.[0].authorName").value("User with id 101"))
                .andExpect(jsonPath("$.items.[0].authorImage").value("image101"))
                .andExpect(jsonPath("$.items.[0].description").value("test description by question 101"))
                .andExpect(jsonPath("$.items.[0].viewCount").value(0))
                .andExpect(jsonPath("$.items.[0].countAnswer").value(1))
                .andExpect(jsonPath("$.items.[0].countValuable").value(-1))
                .andExpect(jsonPath("$.items.[0].isUserBookmark").value(false))

                //??????????????????, ?????? ???????????? QuestionViewDto ?????????? ?????????????????? ???????????? ???????? tags, ?????????????????? ?? ????????
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].name").value("test tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].description").value("description for tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[1].id").value(102))
                .andExpect(jsonPath("$.items.[1].listTagDto.[0].id").value(103));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/getAllSortedQuestionViewDto/answer.yml",
            "datasets/QuestionResourceController/getAllSortedQuestionViewDto/question.yml",
            "datasets/QuestionResourceController/getAllSortedQuestionViewDto/questionHasTag.yml",
            "datasets/QuestionResourceController/getAllSortedQuestionViewDto/reputation.yml",
            "datasets/QuestionResourceController/getAllSortedQuestionViewDto/role.yml",
            "datasets/QuestionResourceController/getAllSortedQuestionViewDto/tag.yml",
            "datasets/QuestionResourceController/getAllSortedQuestionViewDto/user.yml",
            "datasets/QuestionResourceController/getAllSortedQuestionViewDto/voteQuestion.yml",
            "datasets/QuestionResourceController/getAllSortedQuestionViewDto/comment.yml",
            "datasets/QuestionResourceController/getAllSortedQuestionViewDto/commentQuestion.yml",
            "datasets/QuestionResourceController/getAllSortedQuestionViewDto/bookmarks.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getAllSortedQuestionViewDto() throws Exception {
        // ?????????????????? ???????????????????????? Response.
        // ?? ?????????????????? 5 ???????????????? c id 100-105,
        // TagId = 100 ?????????????????? ?? ???????????????? 100, 101, 102
        // TagId = 103 ?????????????????? ?? ???????????????? 104, 105
        // ?????????????? ?????? 101, ?????????????? ?????????????? ?? ???????????????? 100 ?? 103
        // ???????????? c id 102 ?????????? ???????? isDeleted = true
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/sorted/?currentPageNumber=1&itemsOnPage=2&trackedTags=100,103&ignoredTags=101")
                        .header("Authorization", getJwtToken("test_user100@mail.ru", "123")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(3))
                .andExpect(jsonPath("$.totalResultCount").value(5))
                .andExpect(jsonPath("$.itemsOnPage").value(2))

                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionViewDto
                .andExpect(jsonPath("$.items.[0].id").value(101))
                .andExpect(jsonPath("$.items.[1].id").value(105))

                //??????????????????, ?????? ???????????????? ?????????? QuestionViewDto, ????????????????, ?? id 101 ??????????????????
                .andExpect(jsonPath("$.items.[0].title").value("test title by question 101"))
                .andExpect(jsonPath("$.items.[0].authorId").value(101))
                .andExpect(jsonPath("$.items.[0].authorReputation").value(2))
                .andExpect(jsonPath("$.items.[0].authorName").value("User with id 101"))
                .andExpect(jsonPath("$.items.[0].authorImage").value("image101"))
                .andExpect(jsonPath("$.items.[0].description").value("test description by question 101"))
                .andExpect(jsonPath("$.items.[0].viewCount").value(0))
                .andExpect(jsonPath("$.items.[0].countAnswer").value(1))
                .andExpect(jsonPath("$.items.[0].countValuable").value(-1))
                .andExpect(jsonPath("$.items.[0].isUserBookmark").value(true))

                //??????????????????, ?????? ???????????? QuestionViewDto ?????????? ?????????????????? ???????????? ???????? tags, ?????????????????? ?? ????????
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].name").value("test tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].description").value("description for tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[1].id").value(102))
                .andExpect(jsonPath("$.items.[1].listTagDto.[0].id").value(103));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/getAllQuestionViewDtoWithDefaultValuesFromFront/answer.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDtoWithDefaultValuesFromFront/question.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDtoWithDefaultValuesFromFront/questionHasTag.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDtoWithDefaultValuesFromFront/reputation.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDtoWithDefaultValuesFromFront/role.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDtoWithDefaultValuesFromFront/tag.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDtoWithDefaultValuesFromFront/user.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDtoWithDefaultValuesFromFront/voteQuestion.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDtoWithDefaultValuesFromFront/comment.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDtoWithDefaultValuesFromFront/commentQuestion.yml",
            "datasets/QuestionResourceController/getAllQuestionViewDtoWithDefaultValuesFromFront/bookmarks.yml"
    }, cleanBefore = true, cleanAfter = true)
        // ???????? ?????? QuestionResourceController::getAllQuestionViewDto, ???????????? ?????? tracked ?? ignored ?????????? ?? ???????????? ??
        // ?? ?????????????????? ?????????????????????? ?????????????????????? ???? ???????????????? (10)
    void getAllQuestionViewDtoWithDefaultValuesFromFront() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/?currentPageNumber=1")
                        .header("Authorization", getJwtToken("test_user100@mail.ru", "123")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(5))
                .andExpect(jsonPath("$.itemsOnPage").value(10))

                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionViewDto
                .andExpect(jsonPath("$.items.[0].id").value(100))
                .andExpect(jsonPath("$.items.[1].id").value(101))
                .andExpect(jsonPath("$.items.[2].id").value(103))
                .andExpect(jsonPath("$.items.[3].id").value(104))
                .andExpect(jsonPath("$.items.[4].id").value(105))

                //??????????????????, ?????? ???????????????? ?????????? QuestionViewDto, ????????????????, ?? id 100 ??????????????????
                .andExpect(jsonPath("$.items.[0].title").value("test title by question 100"))
                .andExpect(jsonPath("$.items.[0].authorId").value(100))
                .andExpect(jsonPath("$.items.[0].authorReputation").value(6))
                .andExpect(jsonPath("$.items.[0].authorName").value("User with id 100"))
                .andExpect(jsonPath("$.items.[0].authorImage").value("image100"))
                .andExpect(jsonPath("$.items.[0].description").value("test description by question 100"))
                .andExpect(jsonPath("$.items.[0].viewCount").value(0))
                .andExpect(jsonPath("$.items.[0].countAnswer").value(3))
                .andExpect(jsonPath("$.items.[0].countValuable").value(1))
                .andExpect(jsonPath("$.items.[0].isUserBookmark").value(true))

                //??????????????????, ?????? ???????????? QuestionViewDto ?????????? ?????????????????? ???????????? ???????? tags, ?????????????????? ?? ????????
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].name").value("test tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].description").value("description for tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[1].id").value(101))
                .andExpect(jsonPath("$.items.[0].listTagDto.[2].id").value(102))
                .andExpect(jsonPath("$.items.[1].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[1].listTagDto.[1].id").value(102))
                .andExpect(jsonPath("$.items.[2].listTagDto.[0].id").value(101))
                .andExpect(jsonPath("$.items.[3].listTagDto.[0].id").value(103))
                .andExpect(jsonPath("$.items.[4].listTagDto.[0].id").value(103));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/getQuestionCount/question.yml",
            "datasets/QuestionResourceController/getQuestionCount/role.yml",
            "datasets/QuestionResourceController/getQuestionCount/user.yml",
            "datasets/QuestionResourceController/getQuestionCount/answer.yml",
            "datasets/QuestionResourceController/getQuestionCount/reputation.yml",
            "datasets/QuestionResourceController/getQuestionCount/comment.yml",
            "datasets/QuestionResourceController/getQuestionCount/commentQuestion.yml",
            "datasets/QuestionResourceController/getQuestionCount/voteQuestion.yml",
            "datasets/QuestionResourceController/getQuestionCount/questionHasTag.yml",
            "datasets/QuestionResourceController/getQuestionCount/tag.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getQuestionCount() throws Exception {
        this.mvc.perform(get("/api/user/question/count")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(4));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/getQuestionViewDtoSortedByDate/question.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoSortedByDate/questionHasTag.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoSortedByDate/role.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoSortedByDate/tag.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoSortedByDate/voteQuestion.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoSortedByDate/user.yml",
            "datasets/QuestionResourceController/getQuestionViewDtoSortedByDate/bookmarks.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void getQuestionViewDtoSortedByDate() throws Exception {
        //???????????????? ?????? tags, ?????????? ???????????????? ?????? 4 ??????????????
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/new?page=1")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(4))
                .andExpect(jsonPath("$.itemsOnPage").value(10))

                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionViewDto
                .andExpect(jsonPath("$.items.[0].id").value(100))
                .andExpect(jsonPath("$.items.[1].id").value(101))
                .andExpect(jsonPath("$.items.[2].id").value(103))
                .andExpect(jsonPath("$.items.[3].id").value(104))

                //??????????????????, ?????? ???????????????? ?????????? QuestionDto, ????????????????, ?? id 100 ??????????????????
                .andExpect(jsonPath("$.items.[0].title").value("title by question 100"))
                .andExpect(jsonPath("$.items.[0].authorId").value(100))
                .andExpect(jsonPath("$.items.[0].authorName").value("User with id 100"))
                .andExpect(jsonPath("$.items.[0].authorImage").value("image100"))
                .andExpect(jsonPath("$.items.[0].description").value("description by question 100"))
                .andExpect(jsonPath("$.items.[0].viewCount").value(0))
                .andExpect(jsonPath("$.items.[0].countAnswer").value(0))
                .andExpect(jsonPath("$.items.[0].countValuable").value(1))
                .andExpect(jsonPath("$.items.[0].isUserBookmark").value(true))


                //??????????????????, ?????? ???????????? QuestionDto ?????????? ?????????????????? ???????????? ???????? tags, ?????????????????? ?? ??????
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[0].listTagDto.[1].id").value(101));

        //??????????????????, ?????? ???????????????? ???????????? ??????????????, ???????????????????? tags ?? id 100
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/new?page=1&trackedTags=100")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2))
                .andExpect(jsonPath("$.itemsOnPage").value(10))

                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionDto
                .andExpect(jsonPath("$.items.[0].id").value(100));

        //??????????????????, ?????? ???? ignoredTags ???????????????? ???????????? question ?? id 103 ?? 104
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/new?page=1&ignoredTags=100")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2))
                .andExpect(jsonPath("$.itemsOnPage").value(10))

                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionViewDto
                .andExpect(jsonPath("$.items.[0].id").value(103))
                .andExpect(jsonPath("$.items.[1].id").value(104));

        //??????????????????, ?????? ???? trackedTags ?? ???? ignoredTags ???????????????? ???????????? question ?? id 100 ?? 104
        this.mvc.perform(MockMvcRequestBuilders
                        .get("/api/user/question/new?page=1&trackedTags=100,103&ignoredTags=102")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(3))
                .andExpect(jsonPath("$.itemsOnPage").value(10))

                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionViewDto
                .andExpect(jsonPath("$.items.[0].id").value(100))
                .andExpect(jsonPath("$.items.[1].id").value(101))
                .andExpect(jsonPath("$.items.[2].id").value(104));

        //???????????????? ??????????????, ???? ?????????????? items ???? ????????????????????, ?? ???????????? ???????????? ???? trackedTags
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/new?page=1&trackedTags=104")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(0))
                .andExpect(jsonPath("$.totalResultCount").value(0))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                .andExpect(jsonPath("$.items", hasSize(0)));

        /*???????????????? ??????????????, ?????????? tracked = 100,101, ?? ignored = 101, ???????????????????????????? ???????????? ?????????????????? ???????????? ??????????????
                ?? tag 101, ?? ???????????? question ?? id 101*/
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/new?page=1&trackedTags=100,101&ignoredTags=101")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())

                //?????????????????? ?????????????????? PageDto
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.itemsOnPage").value(10))
                .andExpect(jsonPath("$.items.[0].id").value(101));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/testCreateQuestionWithEmptyOrNullTitle/role.yml",
            "datasets/QuestionResourceController/testCreateQuestionWithEmptyOrNullTitle/user.yml",
            "datasets/QuestionResourceController/testCreateQuestionWithEmptyOrNullTitle/tag.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void testCreateQuestionWithEmptyOrNullTitle() throws Exception {
        List<String> tagNames = List.of("one", "two", "three");
        QuestionCreateDto questionCreateDtoEmptyTitle = new QuestionCreateDto(
                "",
                "description",
                tagNames
        );
        QuestionCreateDto questionCreateDtoNullTitle = new QuestionCreateDto(
                null,
                "description",
                tagNames
        );

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDtoEmptyTitle))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException,
                        "should be MethodArgumentNotValidException exception")
                );

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDtoNullTitle))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException,
                        "should be MethodArgumentNotValidException exception")
                );
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/testCreateQuestionWithEmptyOrNullDescription/role.yml",
            "datasets/QuestionResourceController/testCreateQuestionWithEmptyOrNullDescription/user.yml",
            "datasets/QuestionResourceController/testCreateQuestionWithEmptyOrNullDescription/tag.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void testCreateQuestionWithEmptyOrNullDescription() throws Exception {
        List<String> tagNames = List.of("one", "two", "three");
        QuestionCreateDto questionCreateDtoEmptyDescription = new QuestionCreateDto(
                "title",
                "",
                tagNames
        );
        QuestionCreateDto questionCreateDtoNullDescription = new QuestionCreateDto(
                "title",
                null,
                tagNames
        );

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDtoEmptyDescription))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException,
                        "should be MethodArgumentNotValidException exception")
                );

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDtoNullDescription))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException,
                        "should be MethodArgumentNotValidException exception")
                );
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/testCreateQuestionWithEmptyOrNullTags/role.yml",
            "datasets/QuestionResourceController/testCreateQuestionWithEmptyOrNullTags/user.yml",
            "datasets/QuestionResourceController/testCreateQuestionWithEmptyOrNullTags/tag.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void testCreateQuestionWithEmptyOrNullTags() throws Exception {
        QuestionCreateDto questionCreateDtoEmptyTags = new QuestionCreateDto(
                "title",
                "description",
                List.of()
        );
        QuestionCreateDto questionCreateDtoNullTags = new QuestionCreateDto(
                "title",
                "description",
                null
        );

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDtoEmptyTags))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException,
                        "should be MethodArgumentNotValidException exception")
                );

        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDtoNullTags))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException,
                        "should be MethodArgumentNotValidException exception")
                );
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/testCreateQuestionWithNewTags/role.yml",
            "datasets/QuestionResourceController/testCreateQuestionWithNewTags/user.yml",
            "datasets/QuestionResourceController/testCreateQuestionWithNewTags/tag.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void testCreateQuestionWithNewTags() throws Exception {
        List<String> tagNames = List.of("one", "two", "three");
        QuestionCreateDto questionCreateDto = new QuestionCreateDto(
                "title",
                "description",
                tagNames
        );
        MvcResult result = mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isOk())
                .andReturn();

        List<HashMap<String, Object>> tags = JsonPath.parse(result.getResponse().getContentAsString())
                .read("$.listTagDto");
        long expectedCount = 10L + tagNames.size();
        assertEquals(
                expectedCount,
                (long) em.createQuery("select count(t) from Tag t").getSingleResult(),
                "tag table should have" + expectedCount + " rows"
        );
        assertEquals(tagNames.size(), tags.size(), "return tags count dont match");
        tags.forEach(tag -> assertTrue(tagService.getById(Long.valueOf((Integer) tag.get("id"))).isPresent(),
                "there is no '" + tag.get("name") + "' in tag table"));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/testCreateQuestionWithExistingTags/role.yml",
            "datasets/QuestionResourceController/testCreateQuestionWithExistingTags/user.yml",
            "datasets/QuestionResourceController/testCreateQuestionWithExistingTags/tag.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void testCreateQuestionWithExistingTags() throws Exception {
        List<String> tagNames = List.of("tag104", "tag105");
        QuestionCreateDto questionCreateDto = new QuestionCreateDto(
                "title",
                "description",
                tagNames
        );
        MvcResult result = mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isOk())
                .andReturn();
        List<HashMap<String, Object>> tags = JsonPath.parse(result.getResponse().getContentAsString())
                .read("$.listTagDto");

        long expectedCount = 10L;
        assertEquals(
                expectedCount,
                (long) em.createQuery("select count(t) from Tag t").getSingleResult(),
                "tag table should have" + expectedCount + " rows"
        );
        assertEquals(tagNames.size(), tags.size(), "return tags count dont match");
        tags.forEach(tag -> assertTrue(tagService.getById(Long.valueOf((Integer) tag.get("id"))).isPresent(),
                "there is no '" + tag.get("name") + "' in tag table"));
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/testCreateQuestionPresentInDb/role.yml",
            "datasets/QuestionResourceController/testCreateQuestionPresentInDb/user.yml",
            "datasets/QuestionResourceController/testCreateQuestionPresentInDb/tag.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void testCreateQuestionPresentInDb() throws Exception {
        List<String> tagNames = List.of("one", "two", "three");
        QuestionCreateDto questionCreateDto = new QuestionCreateDto(
                "title",
                "description",
                tagNames
        );
        MvcResult result = mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("user@gmail.com", "123")))
                .andExpect(status().isOk())
                .andReturn();
        Long id = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id", Long.class);
        assertTrue(questionService.getById(id).isPresent(), "there's no question in db after creation");
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/testCreateQuestionByAdmin/role.yml",
            "datasets/QuestionResourceController/testCreateQuestionByAdmin/user.yml",
            "datasets/QuestionResourceController/testCreateQuestionByAdmin/tag.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void testCreateQuestionByAdmin() throws Exception {
        List<String> tagNames = List.of("one", "two", "three");
        QuestionCreateDto questionCreateDto = new QuestionCreateDto(
                "title",
                "description",
                tagNames
        );
        mvc.perform(post("/api/user/question")
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("admin@gmail.com", "123")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DataSet(value = {"datasets/QuestionResourceController/getAllQuestionsByVoteAndAnswerAndViewByWeek/answer.yml",
            "datasets/QuestionResourceController/getAllQuestionsByVoteAndAnswerAndViewByWeek/question.yml",
            "datasets/QuestionResourceController/getAllQuestionsByVoteAndAnswerAndViewByWeek/questionHasTag.yml",
            "datasets/QuestionResourceController/getAllQuestionsByVoteAndAnswerAndViewByWeek/reputation.yml",
            "datasets/QuestionResourceController/getAllQuestionsByVoteAndAnswerAndViewByWeek/role.yml",
            "datasets/QuestionResourceController/getAllQuestionsByVoteAndAnswerAndViewByWeek/tag.yml",
            "datasets/QuestionResourceController/getAllQuestionsByVoteAndAnswerAndViewByWeek/user.yml",
            "datasets/QuestionResourceController/getAllQuestionsByVoteAndAnswerAndViewByWeek/voteQuestion.yml",
            "datasets/QuestionResourceController/getAllQuestionsByVoteAndAnswerAndViewByWeek/comment.yml",
            "datasets/QuestionResourceController/getAllQuestionsByVoteAndAnswerAndViewByWeek/commentQuestion.yml",
            "datasets/QuestionResourceController/getAllQuestionsByVoteAndAnswerAndViewByWeek/bookmarks.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getAllQuestionsByVoteAndAnswerByWeek() throws Exception {

        // ???????????????? ????????????????, ?????? ???????????? ????????

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/sortedByWeek/?currentPageNumber=1&itemsOnPage=2&trackedTags=100,103&ignoredTags=102")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(2))
                .andExpect(jsonPath("$.totalResultCount").value(4))
                .andExpect(jsonPath("$.itemsOnPage").value(2))
                .andExpect(jsonPath("$.items.length()").value(2L))
                .andExpect(jsonPath("$.items.[0].id").value(101))
                .andExpect(jsonPath("$.items.[1].id").value(102));

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/sortedByWeek/?currentPageNumber=1&itemsOnPage=2")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/sortedByWeek/?currentPageNumber=1&itemsOnPage=2&trackedTags=100,103")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/sortedByWeek/?currentPageNumber=1&itemsOnPage=2&ignoredTags=102")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/sortedByWeek/?itemsOnPage=2&trackedTags=100,103&ignoredTags=102")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isBadRequest());

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/sortedByWeek/?itemsOnPage=2&trackedTags=100,103&ignoredTags=102")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isBadRequest());

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/sortedByWeek/?itemsOnPage=2&ignoredTags=102")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isBadRequest());

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/sortedByWeek/?itemsOnPage=2&trackedTags=100")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/testCreateBookmark/user.yml",
            "datasets/QuestionResourceController/testCreateBookmark/question.yml",
            "datasets/QuestionResourceController/testCreateBookmark/role.yml"
    }, cleanBefore = true, cleanAfter = true)
    void testCreateBookmark() throws Exception {
        //???????????????? ???????????????? ?????? 100 ?????????? ???? 100 ????????????
        mvc.perform(post("/api/user/question/100/bookmark")
                        .header("Authorization", getJwtToken("user1@gmail.com", "123")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("???????????? ?? Id:100 ?????? ???????????????????????? ?? Id:100 ???????????????? ?? ????????????????")))
                .andReturn();

        //?????????????????? ???????????????? ???? ????
        BookMarks bookMarks = em.createQuery("SELECT a FROM BookMarks a " +
                        "WHERE a.question.id = :questionId " +
                        "AND a.user.id = :userId", BookMarks.class)
                .setParameter("questionId", 100L)
                .setParameter("userId", 100L)
                .getSingleResult();
        Assertions.assertNotNull(bookMarks);

        //???????????????? ???????????????? ?????? 100 ?????????? ???? ???????????????????????????? ????????????
        mvc.perform(post("/api/user/question/99/bookmark")
                        .header("Authorization", getJwtToken("user1@gmail.com", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("???????????????? ???? ??????????????????, ?????????????? ID 99 ???? ????????????????????")));

    }

    @Test
    @DataSet(value = {"datasets/QuestionResourceController/getAllQuestionsDtoByVoteAndAnswerAndViewByMonth/answer.yml",
            "datasets/QuestionResourceController/getAllQuestionsDtoByVoteAndAnswerAndViewByMonth/question.yml",
            "datasets/QuestionResourceController/getAllQuestionsDtoByVoteAndAnswerAndViewByMonth/questionHasTag.yml",
            "datasets/QuestionResourceController/getAllQuestionsDtoByVoteAndAnswerAndViewByMonth/reputation.yml",
            "datasets/QuestionResourceController/getAllQuestionsDtoByVoteAndAnswerAndViewByMonth/role.yml",
            "datasets/QuestionResourceController/getAllQuestionsDtoByVoteAndAnswerAndViewByMonth/tag.yml",
            "datasets/QuestionResourceController/getAllQuestionsDtoByVoteAndAnswerAndViewByMonth/user.yml",
            "datasets/QuestionResourceController/getAllQuestionsDtoByVoteAndAnswerAndViewByMonth/voteQuestion.yml",
            "datasets/QuestionResourceController/getAllQuestionsDtoByVoteAndAnswerAndViewByMonth/comment.yml",
            "datasets/QuestionResourceController/getAllQuestionsDtoByVoteAndAnswerAndViewByMonth/commentQuestion.yml",
            "datasets/QuestionResourceController/getAllQuestionsDtoByVoteAndAnswerAndViewByMonth/bookmarks.yml"
    }, cleanBefore = true, cleanAfter = true)
    void getAllQuestionsByVoteAndAnswerAndViewByMonth() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/sortedByMonth/?currentPageNumber=1&itemsOnPage=2")
                        .header("Authorization", getJwtToken("test_user100@mail.ru", "123")))
                .andExpect(status().isOk())
                //?????????????????? ?????????????????? PageDto, ?? ???????? ???? ???????????????? ?????????????????? ?? ?? ?????????? ???????????? 30 ????????
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(2))
                .andExpect(jsonPath("$.totalResultCount").value(4))
                .andExpect(jsonPath("$.itemsOnPage").value(2))
                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionViewDto
                .andExpect(jsonPath("$.items.[0].id").value(101))
                .andExpect(jsonPath("$.items.[1].id").value(105))
                //??????????????????, ?????? ???????????????? ?????????? QuestionViewDto, ????????????????, ?? id 101 ??????????????????
                .andExpect(jsonPath("$.items.[0].title").value("test title by question 101"))
                .andExpect(jsonPath("$.items.[0].authorId").value(101))
                .andExpect(jsonPath("$.items.[0].authorReputation").value(2))
                .andExpect(jsonPath("$.items.[0].authorName").value("User with id 101"))
                .andExpect(jsonPath("$.items.[0].authorImage").value("image101"))
                .andExpect(jsonPath("$.items.[0].description").value("test description by question 101"))
                .andExpect(jsonPath("$.items.[0].viewCount").value(0))
                .andExpect(jsonPath("$.items.[0].countAnswer").value(1))
                .andExpect(jsonPath("$.items.[0].countValuable").value(-1))
                .andExpect(jsonPath("$.items.[0].isUserBookmark").value(true))
                //??????????????????, ?????? ???????????? QuestionViewDto ?????????? ?????????????????? ???????????? ???????? tags, ?????????????????? ?? ????????
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].name").value("test tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].description").value("description for tag 100"))
                .andExpect(jsonPath("$.items.[0].listTagDto.[0].id").value(100))
                .andExpect(jsonPath("$.items.[1].listTagDto.[0].id").value(103));

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/sortedByMonth/?currentPageNumber=1&itemsOnPage=2&trackedTags=100&ignoredTags=102")
                        .header("Authorization", getJwtToken("test_user100@mail.ru", "123")))
                .andExpect(status().isOk())
                //?????????????????? ?????????????????? PageDto, ?? ???????? ???????????????? ?????????????? ???????? ?? ???? ???????????????? ????????????????????????
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2))
                .andExpect(jsonPath("$.itemsOnPage").value(2))
                //??????????????????, ?????? ?? pageDto ?????????????????????? ???????????? QuestionViewDto
                .andExpect(jsonPath("$.items.[0].id").value(101))
                .andExpect(jsonPath("$.items.[1].id").value(100));
    }


    @Test
    @DataSet(value = {
            "datasets/QuestionResourceController/testAddCommentToQuestion/role.yml",
            "datasets/QuestionResourceController/testAddCommentToQuestion/answer.yml",
            "datasets/QuestionResourceController/testAddCommentToQuestion/reputation.yml",
            "datasets/QuestionResourceController/testAddCommentToQuestion/user.yml",
            "datasets/QuestionResourceController/testAddCommentToQuestion/question.yml",
            "datasets/QuestionResourceController/testAddCommentToQuestion/questionHasTag.yml",
            "datasets/QuestionResourceController/testAddCommentToQuestion/tag.yml",
            "datasets/QuestionResourceController/testAddCommentToQuestion/comment.yml",
            "datasets/QuestionResourceController/testAddCommentToQuestion/commentQuestion.yml",
            "datasets/QuestionResourceController/testAddCommentToQuestion/voteQuestion.yml",

    }, cleanBefore = true, cleanAfter = true)
    void addCommentToQuestion() throws Exception {

        //???????????????? ??????????????
        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/100/comment")
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isOk());

        //?????????????????????? ???? ????????????????????
        this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/555/comment")
                        .content(objectMapper.writeValueAsString("CommentTest"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isBadRequest());


        //?????????? ?????????????????????? ?? ?????????????? ?? id = 100
        Assertions.assertTrue(em.createQuery("SELECT a FROM CommentQuestion a WHERE a.comment.id = :comment_id")
                .setParameter("comment_id", 100L)
                .getResultList().size() > 0);

        //?????????????????????? - ???????????? ????????????
        this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/555/comment")
                        .content(objectMapper.writeValueAsString(""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isBadRequest());


        //null
        this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/comment")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getJwtToken("3user@mail.ru", "3111")))
                .andExpect(status().isBadRequest());
    }
}