package br.com.onetalk.api.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@Builder
public class Participant {

    private ObjectId id;
    private String name;
    private String email;

}
