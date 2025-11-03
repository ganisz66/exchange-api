package pl.szlify.exchangeapi.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import pl.szlify.exchangeapi.config.TestAsyncConfig;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WireMockTest(httpPort = 8089)
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestAsyncConfig.class)
@ActiveProfiles("test")
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static GreenMail smtpServer;

    @BeforeAll
    public static void setupSmtpServer() {
        ServerSetup serverSetup = new ServerSetup(2525, "localhost", ServerSetup.PROTOCOL_SMTP);
        smtpServer = new GreenMail(serverSetup);
        smtpServer.start();
    }

    @AfterAll
    public static void stopSmtpServer() {
        smtpServer.stop();
    }

    @DynamicPropertySource
    static void registerMailProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> 2525);
        registry.add("spring.mail.properties.mail.smtp.auth", () -> false);
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> false);
    }

    @BeforeEach
    void setup() {
        stubFor(WireMock.get(urlEqualTo("/symbols"))
                .willReturn(okJson("""
                                        {
                                          "symbols": {
                                            "USD": "US Dollar",
                                            "PLN": "Polish Zloty"
                                          }
                                        }
                        """)));
    }

    @Test
    void testConvertCurrency_ResultInConvertedCurrencyBeingReturned() throws Exception {

        stubFor(WireMock.get(urlEqualTo("/convert?from=USD&to=PLN&amount=250&date=2025-11-01"))
                .willReturn(okJson("""
                        {
                          "date": "2025-11-01",
                          "historical": "false",
                          "info": { "rate": 3.75, "timestamp": 1762012564 },
                          "query": { "amount": 250, "from": "USD", "to": "PLN" },
                          "result": 937.5,
                          "success": true
                        }
                        """)));

        mockMvc.perform(get("/api/v1/currencies/convert")
                        .param("from", "USD")
                        .param("to", "PLN")
                        .param("amount", "250")
                        .param("date", "2025-11-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value(937.5))
                .andExpect(jsonPath("$.info.rate").value(3.75))
                .andExpect(jsonPath("$.success").value(true));

        smtpServer.waitForIncomingEmail(5000, 1);

        MimeMessage[] receivedMessages = smtpServer.getReceivedMessages();
        assertThat(receivedMessages).hasSize(1);

        MimeMessage message = receivedMessages[0];
        assertThat(message.getSubject()).isEqualTo("Convert confirmation");

        String body = (String) message.getContent();
        assertThat(body).contains("from: USD");
        assertThat(body).contains("to: PLN");
        assertThat(body).contains("amount: 250");
        assertThat(body).contains("rate of 3,75");
        assertThat(body).contains("937.50");
    }

    @Test
    void testFluctuation_ResultInCurrencyBeingReturnedWithFluctuation() throws Exception {
        stubFor(WireMock.get(urlEqualTo("/fluctuation?base=PLN&symbols=PLN%2CUSD&end_date=2025-04-21&start_date=2024-04-22"))
                .willReturn(okJson("""
                         {
                            "start_date": "2024-04-22",
                            "end_date": "2025-04-21",
                            "base": "PLN",
                            "rates": {
                                "PLN": {
                                    "start_rate": 1,
                                    "end_rate": 1,
                                    "change": 0,
                                    "change_pct": 0
                                },
                                "USD": {
                                    "start_rate": 0.246801,
                                    "end_rate": 0.269596,
                                    "change": 0.0228,
                                    "change_pct": 9.2362
                                }
                            },
                            "success": true
                         }                       
                        """)));

        mockMvc.perform(get("/api/v1/currencies/fluctuation")
                        .param("base", "PLN")
                        .param("symbols", "PLN, USD")
                        .param("endDate", "2025-04-21")
                        .param("startDate", "2024-04-22")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.base").value("PLN"))
                .andExpect(jsonPath("$.rates.PLN.start_rate").value(1))
                .andExpect(jsonPath("$.rates.USD.start_rate").value(0.246801))
                .andExpect(jsonPath("$.rates.USD.change_pct").value(9.2362))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void latestTest_ResultInLatestCurrencyBeingReturned() throws Exception {
        stubFor(WireMock.get(urlEqualTo("/latest?base=PLN&symbols=USD"))
                .willReturn(okJson("""
                         {
                              "base": "PLN",
                              "date": "2025-11-03",
                              "rates": {
                                  "USD": 0.270323
                              },
                              "success": true,
                              "timestamp": 1762178715
                         }                       
                        """)));

        mockMvc.perform(get("/api/v1/currencies/latest")
                        .param("baseCurrency", "PLN")
                        .param("symbols", "USD")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.base").value("PLN"))
                .andExpect(jsonPath("$.rates.USD").value(0.270323))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").value(1762178715));
    }

    @Test
    void symbolsTest_ResultInSymbolsBeingReturned() throws Exception {
        mockMvc.perform(get("/api/v1/currencies/symbols"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.symbols.USD").value("US Dollar"))
                .andExpect(jsonPath("$.symbols.PLN").value("Polish Zloty"));
    }

    @Test
    void timeseriesTest_ResultInCurrencyTimeseriesBeingReturned() throws Exception {
        stubFor(WireMock.get(urlEqualTo("/timeseries?base=PLN&symbols=PLN%2CUSD&end_date=2025-04-21&start_date=2025-04-20"))
                .willReturn(okJson("""
                         {
                             "base": "PLN",
                             "start_date": "2025-04-20",
                             "end_date": "2025-04-21",
                             "success": true,
                             "timeseries": true,
                             "rates": {
                                 "2025-04-20": {
                                     "PLN": 1,
                                     "USD": 0.265787
                                 },
                                 "2025-04-21": {
                                     "PLN": 1,
                                     "USD": 0.269596
                                 }
                             }
                         }                       
                        """)));

        mockMvc.perform(get("/api/v1/currencies/timeseries")
                        .param("base", "PLN")
                        .param("symbols", "PLN,USD")
                        .param("endDate", "2025-04-21")
                        .param("startDate", "2025-04-20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.base").value("PLN"))
                .andExpect(jsonPath("$.start_date").value("2025-04-20"))
                .andExpect(jsonPath("$.end_date").value("2025-04-21"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timeseries").value(true))
                .andExpect(jsonPath("$.rates['2025-04-20'].PLN").value(1))
                .andExpect(jsonPath("$.rates['2025-04-20'].USD").value(0.265787))
                .andExpect(jsonPath("$.rates['2025-04-21'].PLN").value(1))
                .andExpect(jsonPath("$.rates['2025-04-21'].USD").value(0.269596));
    }

    @Test
    void historicalDateTest_ResultInCurrencyWithHistoricalDateBeingReturned() throws Exception {
        stubFor(WireMock.get(urlEqualTo("/2025-04-21?symbols=PLN%2CUSD"))
                .willReturn(okJson("""
                         {
                             "base": "EUR",
                             "date": "2025-04-21",
                             "historical": true,
                             "rates": {
                                 "PLN": 4.269893,
                                 "USD": 1.151148
                             },
                             "success": true,
                             "timestamp": 1745279999
                         }                       
                        """)));

        mockMvc.perform(get("/api/v1/currencies/2025-04-21")
                        .param("base", "EUR")
                        .param("symbols", "PLN,USD")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.base").value("EUR"))
                .andExpect(jsonPath("$.date").value("2025-04-21"))
                .andExpect(jsonPath("$.historical").value(true))
                .andExpect(jsonPath("$.rates.PLN").value(4.269893))
                .andExpect(jsonPath("$.rates.USD").value(1.151148))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").value(1745279999));
    }
}
