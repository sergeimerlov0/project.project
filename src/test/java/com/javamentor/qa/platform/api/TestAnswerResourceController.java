package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestAnswerResourceController extends AbstractApiTest {

    @Test
    @DataSet(value = {
            "datasets/AnswerResourceController/answer.yml",
            "datasets/AnswerResourceController/tag.yml",
            "datasets/AnswerResourceController/user.yml",
            "datasets/AnswerResourceController/role.yml",
            "datasets/AnswerResourceController/question.yml",
            "datasets/AnswerResourceController/questionHasTag.yml",
            "datasets/AnswerResourceController/reputation.yml",
            "datasets/AnswerResourceController/voteAnswer.yml"
    })
    void deleteAnswerById() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.delete("/api/user/question/100/answer/100")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isOk());

        //Проверяем, что ответа с id=100 в базе не существует
        Assertions.assertFalse(((long) em.createQuery("SELECT COUNT(e) FROM " + Answer.class.getName() +
                        " e WHERE e.id = 100")
                .getSingleResult()) > 0);
    }

    @Test
    @DataSet(value = {
            "datasets/AnswerResourceController/answer.yml",
            "datasets/AnswerResourceController/tag.yml",
            "datasets/AnswerResourceController/user.yml",
            "datasets/AnswerResourceController/role.yml",
            "datasets/AnswerResourceController/question.yml",
            "datasets/AnswerResourceController/questionHasTag.yml",
            "datasets/AnswerResourceController/reputation.yml",
            "datasets/AnswerResourceController/voteAnswer.yml"
    })
    void tryToDeleteNonExistedId() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.delete("/api/user/question/100/answer/104")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {
            "datasets/AnswerResourceController/getAnswerDatasets/answer.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/question.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/questionHasTag.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/tag.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/reputation.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/role.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/user.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/voteAnswer.yml"
    })
    public void getAnswerByQuestionId() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/100/answer")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].userReputation", is(23)))
                .andExpect(jsonPath("$[0].countValuable", is(2)))
                .andExpect(jsonPath("$[1].id", is(101)))
                .andExpect(jsonPath("$[1].userReputation", is(106)))
                .andExpect(jsonPath("$[1].countValuable", is(-2)));
    }

    @Test
    @DataSet(value = {
            "datasets/AnswerResourceController/getAnswerDatasets/answer.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/question.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/questionHasTag.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/tag.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/reputation.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/role.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/user.yml",
            "datasets/AnswerResourceController/getAnswerDatasets/voteAnswer.yml"
    })
    public void getEmptyListAnswerByQuestionId() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders.get("/api/user/question/2000/answer")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DataSet(value = {
            "datasets/AnswerResourceController/votingApiDatasets/answer.yml",
            "datasets/AnswerResourceController/votingApiDatasets/question.yml",
            "datasets/AnswerResourceController/votingApiDatasets/questionHasTag.yml",
            "datasets/AnswerResourceController/votingApiDatasets/tag.yml",
            "datasets/AnswerResourceController/votingApiDatasets/reputation.yml",
            "datasets/AnswerResourceController/votingApiDatasets/role.yml",
            "datasets/AnswerResourceController/votingApiDatasets/user.yml",
            "datasets/AnswerResourceController/votingApiDatasets/voteAnswer.yml"
    })
    public void setUpVoteAnswerByAnswerId() throws Exception {

        //Проверяем возвращаемое значение. В датасетах в базе данных уже было 2 голоса ЗА ответ с id 100
        this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/upVote")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        //Проверяем, что в БД появилась запись о голосовании от пользователя с id 100 (наш авторизованный юзер) по ответу с id 100
        Assertions.assertTrue(em.createQuery("select v.vote from VoteAnswer v where v.user.id=:user and v.answer.id=:answer")
                .setParameter("user", 100L)
                .setParameter("answer", 100L)
                .getSingleResult()
                .toString()
                .contentEquals("UP_VOTE"));

        //Проверяем, что в БД изменилась репутация пользователя с id 101 (автор) по ответу с id 100. В датасетах изначальная репутация была 106
        Assertions.assertTrue(em.createQuery("select sum(r.count) from Reputation r where r.author.id=:author")
                .setParameter("author", 101L)
                .getSingleResult()
                .toString()
                .contentEquals("116"));

        //Проверяем, что невозможно проголосовать за свой ответ. Ответ с id 100 принадлежит пользователю с id 101("test2@test.ru","123")
        Assertions.assertTrue(this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/upVote")
                        .header("Authorization", getJwtToken("test2@test.ru","123")))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().contains("Voting for your answer with id " + 100 + " not allowed"));

        //проверяем невозможность проголосовать дважды за один ответ, как за, так и против
        Assertions.assertTrue(this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/upVote")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().contains("ConstraintViolationException"));

        Assertions.assertTrue(this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/downVote")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().contains("ConstraintViolationException"));
    }

    @Test
    @DataSet(value = {
            "datasets/AnswerResourceController/votingApiDatasets/answer.yml",
            "datasets/AnswerResourceController/votingApiDatasets/question.yml",
            "datasets/AnswerResourceController/votingApiDatasets/questionHasTag.yml",
            "datasets/AnswerResourceController/votingApiDatasets/tag.yml",
            "datasets/AnswerResourceController/votingApiDatasets/reputation.yml",
            "datasets/AnswerResourceController/votingApiDatasets/role.yml",
            "datasets/AnswerResourceController/votingApiDatasets/user.yml",
            "datasets/AnswerResourceController/votingApiDatasets/voteAnswer.yml"
    })
    public void setDownVoteAnswerByAnswerId() throws Exception {

        //Проверяем возвращаемое значение. В датасетах в базе данных уже было 2 голоса ЗА ответ с id 100
        this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/downVote")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        //Проверяем, что в БД появилась запись о голосовании от пользователя с id 100 (наш авторизованный юзер) по ответу с id 100
        Assertions.assertTrue(em.createQuery("select v.vote from VoteAnswer v where v.user.id=:user and v.answer.id=:answer")
                .setParameter("user", 100L)
                .setParameter("answer", 100L)
                .getSingleResult()
                .toString()
                .contentEquals("DOWN_VOTE"));

        //Проверяем, что в БД изменилась репутация пользователя с id 101 (автор) по ответу с id 100. В датасетах изначальная репутация была 106
        Assertions.assertTrue(em.createQuery("select sum(r.count) from Reputation r where r.author.id=:author")
                .setParameter("author", 101L)
                .getSingleResult()
                .toString()
                .contentEquals("101"));

        //Проверяем, что невозможно проголосовать за свой ответ. Ответ с id 100 принадлежит пользователю с id 101("test2@test.ru","123")
        Assertions.assertTrue(this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/downVote")
                        .header("Authorization", getJwtToken("test2@test.ru","123")))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().contains("Voting for your answer with id " + 100 + " not allowed"));

        //проверяем невозможность проголосовать дважды за один ответ, как за, так и против
        Assertions.assertTrue(this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/upVote")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().contains("ConstraintViolationException"));

        Assertions.assertTrue(this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/100/downVote")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().contains("ConstraintViolationException"));
    }

    @Test
    @DataSet(value = {
            "datasets/AnswerResourceController/votingApiDatasets/answer.yml",
            "datasets/AnswerResourceController/votingApiDatasets/question.yml",
            "datasets/AnswerResourceController/votingApiDatasets/questionHasTag.yml",
            "datasets/AnswerResourceController/votingApiDatasets/tag.yml",
            "datasets/AnswerResourceController/votingApiDatasets/reputation.yml",
            "datasets/AnswerResourceController/votingApiDatasets/role.yml",
            "datasets/AnswerResourceController/votingApiDatasets/user.yml",
            "datasets/AnswerResourceController/votingApiDatasets/voteAnswer.yml"
    })
    public void addNewAnswer() throws Exception {

        //Проверяем возвращаемое значение.
        this.mvc.perform(MockMvcRequestBuilders.post("/api/user/question/100/answer/add")
                        .header("Authorization", getJwtToken("3user@mail.ru","3111")))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.id").value(102L))
//                .andExpect(jsonPath("$.userId").value(3L))
//                .andExpect(jsonPath("$.image").value("3user.ru/myphoto/1"))
//                .andExpect(jsonPath("$.nickName").value("3user"))
//                .andExpect(jsonPath("$.questionId").value(100L))
//                .andExpect(jsonPath("$.body").value("test"))
//                //.andExpect(jsonPath("$.persistDate").value())
//                .andExpect(jsonPath("$.comments.[1].id").value(101L));

        //Проверяем, что в БД появилась запись о новом ответе с id 100
        Assertions.assertTrue(em.createQuery("select v from VoteAnswer v where v.user.id=:user and v.answer.id=:answer")
                .setParameter("user", 100L)
                .setParameter("answer", 102L)
                .getResultList().size()>0);

    }
}