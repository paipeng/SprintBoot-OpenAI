package com.paipeng.openai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paipeng.openai.config.ApplicationConfig;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Retrofit;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.theokanning.openai.service.OpenAiService.defaultRetrofit;

@RestController
@RequestMapping(value = "/openai")
public class OpenAIController {
    private final Logger log;

    @Autowired
    private ApplicationConfig applicationConfig;

    public OpenAIController() {
        this.log = LoggerFactory.getLogger(this.getClass().getName());
    }
    @GetMapping(value = "/ada/{text}", produces = {"application/json;charset=UTF-8"})
    public String openAI(@PathVariable("text") String text) {
        log.info("openAI: " + text);
        log.info("apiKey: " + applicationConfig.getApiKey());
        OpenAiService service = new OpenAiService(applicationConfig.getApiKey());
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Suggest three names for an animal that is a superhero.\n" +
                        "\n" +
                        "Animal: Cat\n" +
                        "Names: Captain Sharpclaw, Agent Fluffball, The Incredible Feline\n" +
                        "Animal: Dog\n" +
                        "Names: Ruff the Protector, Wonder Canine, Sir Barks-a-Lot\n" +
                        "Animal: " + text + "\n" +
                        "Names:")
                .model("text-davinci-003")
                .echo(false)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);

        return service.createCompletion(completionRequest).getChoices().get(0).getText();
    }


    @GetMapping(value = "/zh/{text}", produces = {"application/json;charset=UTF-8"})
    public String openAIChinese(@PathVariable("text") String text) {
        log.info("openAI: " + text);
        log.info("apiKey: " + applicationConfig.getApiKey());
        OpenAiService service = new OpenAiService(applicationConfig.getApiKey());
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("给自己的宠物起三个女孩名字.\n" +
                        "\n" +
                        "动物: 猫\n" +
                        "名字: 小天使, 小可爱, 小柔\n" +
                        "动物: 狗\n" +
                        "名字: 小花, 黑宝, 冰雪\n" +
                        "动物: " + text + "\n" +
                        "名字:")
                .model("text-davinci-003")
                .echo(false)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);

        return service.createCompletion(completionRequest).getChoices().get(0).getText();
    }



    @GetMapping(value = "/gpt/{text}", produces = {"application/json;charset=UTF-8"})
    public String openAIGPT35(@PathVariable("text") String text) {
        log.info("openAI: " + text);
        log.info("apiKey: " + applicationConfig.getApiKey());

        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        OkHttpClient client = OpenAiService.defaultClient(applicationConfig.getApiKey(), Duration.ofSeconds(15))
                .newBuilder()
                //.interceptor(HttpLoggingInterceptor())
                .build();
        Retrofit retrofit = OpenAiService.defaultRetrofit(client, mapper);

        OpenAiApi api = retrofit.create(OpenAiApi.class);
        OpenAiService service = new OpenAiService(api);



        //OpenAiService service = new OpenAiService(applicationConfig.getApiKey());

        List<ChatMessage> chatMessages = new ArrayList<>();

        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), text);
        //chatMessage.setRole("system");
        //chatMessage.setContent("你是一个得力助手");
        //chatMessages.add(chatMessage);
        //chatMessage = new ChatMessage();
        //chatMessage.setRole("user");
        //chatMessage.setContent(text);
        chatMessages.add(chatMessage);
        //chatMessage.setRole("assistant");
        //chatMessage.setContent("中国韩国电子商务, 进出口，旅游，购物");
        //chatMessages.add(chatMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(chatMessages)
                .model("gpt-3.5-turbo")
                .n(5)
                .maxTokens(80)
                .logitBias(new HashMap<>())
                .build();
        //service.createChatCompletion(chatCompletionRequest).getChoices().forEach(System.out::println);

        return service.createChatCompletion(chatCompletionRequest).getChoices().get(0).toString();
    }
}
