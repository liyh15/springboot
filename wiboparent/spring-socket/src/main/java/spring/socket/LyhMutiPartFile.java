package spring.socket;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class LyhMutiPartFile {

    private byte [] bytes;

    private String name;

    private String originalFileName;

    private String contentType;
}
