package com.preproject.preproject.slice.restdocs;

import com.google.gson.Gson;
import com.preproject.preproject.answers.dto.AnswerResponseDto;
import com.preproject.preproject.answers.entity.Answer;
import com.preproject.preproject.helper.QuestionControllerHelper;
import com.preproject.preproject.helper.RestDocumentationHelper;
import com.preproject.preproject.questions.controller.QuestionController;
import com.preproject.preproject.questions.dto.MultiQuestionResponseDto;
import com.preproject.preproject.questions.dto.QuestionPatchDto;
import com.preproject.preproject.questions.dto.QuestionPostDto;
import com.preproject.preproject.questions.dto.SingleQuestionResponseDto;
import com.preproject.preproject.questions.entity.Question;
import com.preproject.preproject.questions.mapper.QuestionMapper;
import com.preproject.preproject.questions.service.QuestionService;
import com.preproject.preproject.users.dto.UsersResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(QuestionController.class)
@AutoConfigureRestDocs
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private QuestionMapper questionMapper;

    @DisplayName("QuestionController.getQuestion")
    @Test
    public void givenQuestionId_whenGetRequested_thenQuestionReturned() throws Exception {

        //given
        long questionId = 1L;

        UsersResponseDto usersResponseDto =
                UsersResponseDto.builder()
                        .displayName("homer simpson")
                        .userId(1L)
                        .build();

        List<String> tags = List.of(
                "java", "react", "mysql"
        );

        AnswerResponseDto answerResponseDto =
                AnswerResponseDto.builder()
                        .answerId(1L)
                        .content("answer1")
                        .user(
                                UsersResponseDto.builder()
                                        .userId(1L).displayName("user1")
                                        .build()
                        )
                        .build();

        SingleQuestionResponseDto responseDto =
                SingleQuestionResponseDto.builder()
                        .questionId(questionId)
                        .title("question 1")
                        .description("this is question 1")
                        .tags(tags)
                        .user(usersResponseDto)
                        .likes(5)
                        .dislikes(1)
                        .views(10)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .answers(List.of(answerResponseDto))
                        .build();

        given(questionService.getQuestion(Mockito.anyLong())).willReturn(Mockito.mock(Question.class));
        given(questionMapper.dtoFrom(Mockito.any(Question.class))).willReturn(responseDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                get(QuestionControllerHelper.URL + "/{questionId}", questionId)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        verify(questionService).getQuestion(Mockito.anyLong());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.questionId").value(responseDto.getQuestionId()))
                .andExpect(jsonPath("$.data.title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$.data.description").value(responseDto.getDescription()))
                .andExpect(jsonPath("$.data.user.userId").value(responseDto.getUser().getUserId()))
                .andExpect(jsonPath("$.data.user.displayName").value(responseDto.getUser().getDisplayName()))
                .andExpect(jsonPath("$.data.tags").isArray())
                .andDo(
                        document(
                                "get-question",
                                RestDocumentationHelper.prettyPrintRequest(),
                                RestDocumentationHelper.prettyPrintResponse(),
                                pathParameters(
                                        parameterWithName("questionId").description("????????? ?????????")
                                ),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.questionId").type(JsonFieldType.NUMBER).description("????????? ?????????").ignored(),
                                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("????????? ??????"),
                                                fieldWithPath("data.likes").type(JsonFieldType.NUMBER).description("????????? ?????? ???"),
                                                fieldWithPath("data.dislikes").type(JsonFieldType.NUMBER).description("????????? ?????? ???"),
                                                fieldWithPath("data.views").type(JsonFieldType.NUMBER).description("?????????"),
                                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("????????? ??????"),
                                                fieldWithPath("data.user").type(JsonFieldType.OBJECT).description("?????????"),
                                                fieldWithPath("data.user.userId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                                fieldWithPath("data.user.displayName").type(JsonFieldType.STRING).description("????????? ?????????"),
                                                fieldWithPath("data.tags[]").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                                fieldWithPath("data.createdAt").type(JsonFieldType.VARIES).description("????????? ????????????"),
                                                fieldWithPath("data.modifiedAt").type(JsonFieldType.VARIES).description("????????? ????????? ????????????"),
                                                fieldWithPath("data.answers[]").type(JsonFieldType.ARRAY).description("???????????? ?????? ??????"),
                                                fieldWithPath("data.answers[].answerId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                                fieldWithPath("data.answers[].content").type(JsonFieldType.STRING).description("?????? ??????"),
                                                fieldWithPath("data.answers[].user").type(JsonFieldType.OBJECT).description("?????? ????????? ????????? ??????"),
                                                fieldWithPath("data.answers[].user.userId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                                fieldWithPath("data.answers[].user.displayName").type(JsonFieldType.STRING).description("????????? ??????")

                                        )
                                )
                        )
                );
    }


    @DisplayName("QuestionController.postQuestion")
    @Test
    public void givenPostDtoWithUserIdAndTags_whenPostRequested_thenNewQuestionReturned() throws Exception {

        //given
        QuestionPostDto requestDto =
                QuestionPostDto.builder()
                        .title("question 1")
                        .description("this is question 1")
                        .tags(List.of("java", "react", "mysql"))
                        .userId(1L)
                        .build();

        String requestBody = gson.toJson(requestDto);

        AnswerResponseDto answerResponseDto =
                AnswerResponseDto.builder()
                        .user(
                                UsersResponseDto.builder()
                                        .build()
                        )
                        .build();

        SingleQuestionResponseDto responseDto =
                SingleQuestionResponseDto.builder()
                        .questionId(1L)
                        .title("question 1")
                        .description("this is question 1")
                        .tags(requestDto.getTags())
                        .user(
                                UsersResponseDto.builder()
                                        .userId(1L)
                                        .displayName("user1")
                                        .build()
                        )
                        .likes(0)
                        .dislikes(0)
                        .views(0)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .answers(List.of(answerResponseDto))
                        .build();

        given(questionMapper.entityFromDto(Mockito.any(QuestionPostDto.class))).willReturn(Mockito.mock(Question.class));
        given(questionService.postQuestion(Mockito.any(Question.class))).willReturn(Mockito.mock(Question.class));
        given(questionMapper.dtoFrom(Mockito.any(Question.class))).willReturn(responseDto);

        //when
        ResultActions resultActions =
                mockMvc
                        .perform(
                                post(QuestionControllerHelper.URL + "/ask")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .content(requestBody)
                        );

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.questionId").value(responseDto.getQuestionId()))
                .andExpect(jsonPath("$.data.title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$.data.description").value(responseDto.getDescription()));

        //doc
        resultActions
                .andDo(
                        document(
                                "post-question",
                                RestDocumentationHelper.prettyPrintRequest(),
                                RestDocumentationHelper.prettyPrintResponse(),
                                requestFields(
                                        List.of(
                                                fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????"),
                                                fieldWithPath("description").type(JsonFieldType.STRING).description("????????? ??????"),
                                                fieldWithPath("tags").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("????????? ?????????")
                                        )
                                ),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.questionId").type(JsonFieldType.NUMBER).description("????????? ?????????").ignored(),
                                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("????????? ??????"),
                                                fieldWithPath("data.likes").type(JsonFieldType.NUMBER).description("????????? ?????? ???"),
                                                fieldWithPath("data.dislikes").type(JsonFieldType.NUMBER).description("????????? ?????? ???"),
                                                fieldWithPath("data.views").type(JsonFieldType.NUMBER).description("?????????"),
                                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("????????? ??????"),
                                                fieldWithPath("data.user").type(JsonFieldType.OBJECT).description("?????????"),
                                                fieldWithPath("data.user.userId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                                fieldWithPath("data.user.displayName").type(JsonFieldType.STRING).description("????????? ?????????"),
                                                fieldWithPath("data.tags[]").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                                fieldWithPath("data.createdAt").type(JsonFieldType.VARIES).description("????????? ????????????"),
                                                fieldWithPath("data.modifiedAt").type(JsonFieldType.VARIES).description("????????? ????????? ????????????"),
                                                fieldWithPath("data.answers[]").type(JsonFieldType.ARRAY).description("???????????? ?????? ??????"),
                                                fieldWithPath("data.answers[].answerId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                                fieldWithPath("data.answers[].content").type(JsonFieldType.VARIES).description("?????? ??????"),
                                                fieldWithPath("data.answers[].user").type(JsonFieldType.VARIES).description("?????? ????????? ????????? ??????"),
                                                fieldWithPath("data.answers[].user.userId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                                fieldWithPath("data.answers[].user.displayName").type(JsonFieldType.VARIES).description("????????? ??????")

                                        )
                                )
                        )
                );
    }

    @DisplayName("QuestionController.updateQuestion")
    @Test
    public void givenDtoAndId_whenPatchRequested_thenQuestionReturned() throws Exception {

        //given
        long questionId = 1L;

        QuestionPatchDto requestDto =
                QuestionPatchDto.builder()
                        .questionId(questionId)
                        .title("updated")
                        .userId(1L)
                        .description("updated desc")
                        .tags(List.of("java", "react", "mysql"))
                        .build();

        AnswerResponseDto answerResponseDto =
                AnswerResponseDto.builder()
                        .user(
                                UsersResponseDto.builder()
                                        .build()
                        )
                        .build();

        SingleQuestionResponseDto responseDto =
                SingleQuestionResponseDto.builder()
                        .questionId(questionId)
                        .title(requestDto.getTitle())
                        .description(requestDto.getDescription())
                        .tags(requestDto.getTags())
                        .user(
                                UsersResponseDto.builder()
                                        .userId(1L)
                                        .displayName("user1")
                                        .build()
                        )
                        .likes(0)
                        .dislikes(0)
                        .views(0)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .answers(List.of(answerResponseDto))
                        .build();

        String request = gson.toJson(requestDto);

        given(questionMapper.entityFromDto(Mockito.any(QuestionPatchDto.class))).willReturn(Mockito.mock(Question.class));
        given(questionService.updateQuestion(Mockito.any(Question.class))).willReturn(Mockito.mock(Question.class));
        given(questionMapper.dtoFrom(Mockito.any(Question.class))).willReturn(responseDto);

        //when

        ResultActions resultActions = mockMvc
                .perform(
                        patch(QuestionControllerHelper.URL + "/{questionId}", questionId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.questionId").value(requestDto.getQuestionId()))
                .andExpect(jsonPath("$.data.title").value(requestDto.getTitle()))
                .andExpect(jsonPath("$.data.description").value(requestDto.getDescription()))
                .andDo(print());

        //doc
        resultActions
                .andDo(
                        document(
                                "patch-question",
                                RestDocumentationHelper.prettyPrintRequest(),
                                RestDocumentationHelper.prettyPrintResponse(),
                                pathParameters(
                                        parameterWithName("questionId").description("????????? ????????? ?????????")
                                ),
                                requestFields(
                                        List.of(
                                                fieldWithPath("questionId").type(JsonFieldType.NUMBER).description("????????? ?????????").ignored(),
                                                fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ????????? ??????").optional(),
                                                fieldWithPath("description").type(JsonFieldType.STRING).description("????????? ????????? ??????").optional(),
                                                fieldWithPath("tags").type(JsonFieldType.ARRAY).description("?????? ??????").optional(),
                                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("????????? ?????????")
                                        )
                                ),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.questionId").type(JsonFieldType.NUMBER).description("????????? ?????????").ignored(),
                                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("????????? ??????"),
                                                fieldWithPath("data.likes").type(JsonFieldType.NUMBER).description("????????? ?????? ???"),
                                                fieldWithPath("data.dislikes").type(JsonFieldType.NUMBER).description("????????? ?????? ???"),
                                                fieldWithPath("data.views").type(JsonFieldType.NUMBER).description("?????????"),
                                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("????????? ??????"),
                                                fieldWithPath("data.user").type(JsonFieldType.OBJECT).description("?????????"),
                                                fieldWithPath("data.user.userId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                                fieldWithPath("data.user.displayName").type(JsonFieldType.STRING).description("????????? ?????????"),
                                                fieldWithPath("data.tags[]").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                                fieldWithPath("data.createdAt").type(JsonFieldType.VARIES).description("????????? ????????????"),
                                                fieldWithPath("data.modifiedAt").type(JsonFieldType.VARIES).description("????????? ????????? ????????????"),
                                                fieldWithPath("data.answers[]").type(JsonFieldType.ARRAY).description("???????????? ?????? ??????"),
                                                fieldWithPath("data.answers[].answerId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                                fieldWithPath("data.answers[].content").type(JsonFieldType.NULL).description("?????? ??????"),
                                                fieldWithPath("data.answers[].user").type(JsonFieldType.OBJECT).description("?????? ????????? ????????? ??????"),
                                                fieldWithPath("data.answers[].user.userId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                                fieldWithPath("data.answers[].user.displayName").type(JsonFieldType.NULL).description("????????? ??????")
                                        )
                                )
                        )
                );
    }

    @DisplayName("QuestionController.getQuestions")
    @Test
    public void givenPageAndTab_whenGetRequest_thenPageReturned() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("tab", null);
        params.add("size", "5");

        List<MultiQuestionResponseDto> list = List.of(
                MultiQuestionResponseDto.builder()
                        .questionId(1L)
                        .description("question1")
                        .tags(List.of("java", "spring", "mysql"))
                        .title("question title1")
                        .likes(5)
                        .dislikes(1)
                        .answers(2)
                        .views(100)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .user(
                                UsersResponseDto.builder()
                                        .userId(1L)
                                        .displayName("user1")
                                        .build())
                        .build());

        given(questionService.getQuestions(Mockito.any(PageRequest.class))).willReturn(Mockito.mock(PageImpl.class));
        given(questionMapper.listDtoFromEntities(Mockito.any(List.class))).willReturn(list);

        //when
        ResultActions resultActions =
                mockMvc
                        .perform(
                                get(QuestionControllerHelper.URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .params(params)
                        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pageInfo.page").value(1))
                .andExpect(jsonPath("$.data[0].tags").isArray())
                .andDo(print());

        resultActions
                .andDo(
                        document(
                                "get-questions",
                                RestDocumentationHelper.prettyPrintRequest(),
                                RestDocumentationHelper.prettyPrintResponse(),
                                requestParameters(
                                        parameterWithName("page").optional().description("????????? ??????. ???????????? 1"),
                                        parameterWithName("tab").optional().description("?????? ??????. ???????????? createdAt"),
                                        parameterWithName("size").optional().description("????????? ??? ????????? ????????? ???. ????????? 5")
                                ),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("????????? ??????"),
                                                fieldWithPath("data[].questionId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("????????? ??????"),
                                                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("????????? ??????"),
                                                fieldWithPath("data[].tags").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                                fieldWithPath("data[].likes").type(JsonFieldType.NUMBER).description("????????? ?????? ???"),
                                                fieldWithPath("data[].dislikes").type(JsonFieldType.NUMBER).description("????????? ?????? ???"),
                                                fieldWithPath("data[].answers").type(JsonFieldType.NUMBER).description("?????? ?????? ???"),
                                                fieldWithPath("data[].views").type(JsonFieldType.NUMBER).description("?????????"),
                                                fieldWithPath("data[].createdAt").type(JsonFieldType.VARIES).description("????????????"),
                                                fieldWithPath("data[].modifiedAt").type(JsonFieldType.VARIES).description("?????? ????????????"),
                                                fieldWithPath("data[].user").type(JsonFieldType.OBJECT).description("????????? ??????"),
                                                fieldWithPath("data[].user.userId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                                fieldWithPath("data[].user.displayName").type(JsonFieldType.STRING).description("????????? ??????"),
                                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("?????????????????? ??????"),
                                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("?????? ????????? ??????"),
                                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("??? ?????? ????????? ????????? ??????"),
                                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("?????? ????????? ??????"),
                                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("??? ????????? ???"),
                                                fieldWithPath("pageInfo.tab").type(JsonFieldType.STRING).description("????????? ?????? ??????(????????? ??? ?????????)")
                                        )
                                )

                        )
                );
    }

    @DisplayName("QuestionController.like")
    @Test
    public void givenQuestionId_whenPatch_thenQuestionLikeUpdated() throws Exception {

        //given

        long questionId = 1L;

        //when
        ResultActions resultActions =
                mockMvc
                        .perform(
                                patch(QuestionControllerHelper.URL + "/{questionId}/like", questionId).param("userId", "1")
                                        .contentType(MediaType.APPLICATION_JSON)
                        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("liked"));

        resultActions
                .andDo(
                        document(
                                "hit-like-button",
                                RestDocumentationHelper.prettyPrintRequest(),
                                RestDocumentationHelper.prettyPrintResponse(),
                                requestParameters(
                                        parameterWithName("userId").description("????????? ?????? ????????? ?????????")
                                ),
                                pathParameters(
                                        parameterWithName("questionId").description("????????? ?????????")
                                ),
                                responseFields(
                                        fieldWithPath("data").type(JsonFieldType.STRING).description("??????")
                                )
                        )
                );

    }

    @DisplayName("QuestionController.dislike")
    @Test
    public void givenQuestionId_whenPatch_thenQuestionDislikeUpdated() throws Exception {

        //given

        long questionId = 1L;

        //when
        ResultActions resultActions =
                mockMvc
                        .perform(
                                patch(QuestionControllerHelper.URL + "/{questionId}/dislike", questionId).param("userId", "1")
                                        .contentType(MediaType.APPLICATION_JSON)
                        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("disliked"));

        resultActions
                .andDo(
                        document(
                                "hit-dislike-button",
                                RestDocumentationHelper.prettyPrintRequest(),
                                RestDocumentationHelper.prettyPrintResponse(),
                                requestParameters(
                                        parameterWithName("userId").description("????????? ?????? ????????? ?????????")
                                ),
                                pathParameters(
                                        parameterWithName("questionId").description("????????? ?????????")
                                ),
                                responseFields(
                                        fieldWithPath("data").type(JsonFieldType.STRING).description("??????")
                                )
                        )
                );

    }

    @DisplayName("QuestionController.delete")
    @Test
    public void givenQuestionIdAndUserId_whenDeleteRequested_thenQuestionDeleted() throws Exception {

        long questionId = 1L;
        long userId  = 1L;
        String response = "delete";

        //when
        ResultActions resultActions =
                mockMvc
                        .perform(
                                delete(QuestionControllerHelper.URL + "/{questionId}", questionId).param("userId", "1")
                                        .contentType(MediaType.APPLICATION_JSON)
                        );


        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(response));

        resultActions
                .andDo(
                        document(
                                "delete-question",
                                RestDocumentationHelper.prettyPrintRequest(),
                                RestDocumentationHelper.prettyPrintResponse(),
                                pathParameters(
                                        parameterWithName("questionId").description("????????? ?????????")
                                ),
                                requestParameters(
                                        parameterWithName("userId").description("????????? ?????????")
                                ),
                                responseFields(
                                        fieldWithPath("data").type(JsonFieldType.STRING).description("??????")
                                )
                        )

                );

    }
}
