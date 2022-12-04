package br.com.rollo.rafael.tuitrapi.application.validators;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImagePathValidatorTest {

    private static final String HTTP_SCHEME = "http://";
    private static final String HTTPS_SCHEME = "https://";
    private static final String STRANGE_SCHEME = "strg://";

    private static final String JPG_EXTENSION = ".jpg";
    private static final String JPEG_EXTENSION = ".jpeg";
    private static final String PNG_EXTENSION = ".png";
    private static final String STRANGE_EXTENSION = ".strg";

    private ImagePathValidator validator = new ImagePathValidator();

    @Test
    public void shouldNotReportInvalidIfInputIsNull() {
        // when input is null
        boolean valid = validator.isValid(null, null);
        // then
        assertThat(valid).isNotEqualTo(false);
    }

    @Test
    public void shouldNotReportInvalidIfInputIsEmpty() {
        // given empty string
        String input = "";
        // when
        boolean valid = validator.isValid(input, null);
        // then
        assertThat(valid).isNotEqualTo(false);
    }

    @Test
    public void shouldReturnValidForHTTPAndJPG() {
        // given
        String input = HTTP_SCHEME + "domain.com/resources/example" + JPG_EXTENSION;
        // when
        boolean valid = validator.isValid(input, null);
        // then
        assertThat(valid).isTrue();
    }

    @Test
    public void shouldReturnValidForHTTPAndJPEG() {
        // given
        String input = HTTP_SCHEME + "domain.com/resources/example" + JPEG_EXTENSION;
        // when
        boolean valid = validator.isValid(input, null);
        // then
        assertThat(valid).isTrue();
    }

    @Test
    public void shouldReturnValidForHTTPAndPNG() {
        // given
        String input = HTTP_SCHEME + "domain.com/resources/example" + PNG_EXTENSION;
        // when
        boolean valid = validator.isValid(input, null);
        // then
        assertThat(valid).isTrue();
    }

    @Test
    public void shouldReturnValidForHTTPSAndJPG() {
        // given
        String input = HTTPS_SCHEME + "domain.com/resources/example" + JPG_EXTENSION;
        // when
        boolean valid = validator.isValid(input, null);
        // then
        assertThat(valid).isTrue();
    }

    @Test
    public void shouldReturnValidForHTTPSAndJPEG() {
        // given
        String input = HTTPS_SCHEME + "domain.com/resources/example" + JPEG_EXTENSION;
        // when
        boolean valid = validator.isValid(input, null);
        // then
        assertThat(valid).isTrue();
    }

    @Test
    public void shouldReturnValidForHTTPSAndPNG() {
        // given
        String input = HTTPS_SCHEME + "domain.com/resources/example" + PNG_EXTENSION;
        // when
        boolean valid = validator.isValid(input, null);
        // then
        assertThat(valid).isTrue();
    }

    @Test
    public void shouldReturnInvalidForStrangeSchemeAndValidFileExtension() {
        // given
        String input = STRANGE_SCHEME + "domain.com/resources/example" + PNG_EXTENSION;
        // when
        boolean valid = validator.isValid(input, null);
        // then
        assertThat(valid).isFalse();
    }

    @Test
    public void shouldReturnInvalidForValidSchemeAndAnotherFileExtension() {
        // given
        String input = HTTPS_SCHEME + "domain.com/resources/example" + STRANGE_SCHEME;
        // when
        boolean valid = validator.isValid(input, null);
        // then
        assertThat(valid).isFalse();
    }
}
