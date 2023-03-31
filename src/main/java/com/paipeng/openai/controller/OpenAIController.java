package com.paipeng.openai.controller;

import com.paipeng.openai.config.ApplicationConfig;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
