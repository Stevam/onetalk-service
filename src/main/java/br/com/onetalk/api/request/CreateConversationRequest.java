package br.com.onetalk.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateConversationRequest {

    private Participant participant1;
    private Participant participant2;

}