package org.swasth.hcx.controllers.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.swasth.common.dto.HeaderAudit;
import org.swasth.common.utils.JSONUtils;
import org.swasth.hcx.controllers.BaseSpec;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.swasth.common.utils.Constants.HCX_ON_SEARCH;
import static org.swasth.common.utils.Constants.HCX_SEARCH;

public class SearchControllerTest extends BaseSpec {

    @Test
    public void searchBadRequest() throws Exception {
        doNothing().when(mockKafkaClient).send(anyString(),anyString(),any());
        String requestBody = getSearchBadRequest();
        MvcResult mvcResult = mockMvc.perform(post(HCX_SEARCH).content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        assertEquals(400, status);
    }

    private String getSearchBadRequest() throws JsonProcessingException {
        Map<String,Object> obj = new HashMap<>();
        obj.put("payload","eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAiLCJ4LWhjeC1zZW5kZXJfY29kZSI6IjEtODA1MDBjZGQtMmRlYy00ZDYwLWJkMWItOGY5ZDgzZjQ5N2ZmIiwieC1oY3gtcmVjaXBpZW50X2NvZGUiOiJoY3gtZ2F0ZXdheS1jb2RlIiwieC1oY3gtd29ya2Zsb3dfaWQiOiIxZTgzLTQ2MGEtNGYwYi1iMDE2LWMyMmQ4MjA2NzRlMSIsIngtaGN4LWFwaV9jYWxsX2lkIjoiMjZiMTA2MGMtMWU4My00NjAwLTk2MTItZWEzMWUwY2E1MDkxIiwieC1oY3gtc3RhdHVzIjoicmVxdWVzdC5pbml0aWF0ZSIsIngtaGN4LWRlYnVnX2ZsYWciOiJJbmZvIiwieC1oY3gtc2VhcmNoIjp7fX0=.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ");
        return JSONUtils.serialize(obj);
    }

    @Test
    public void searchSuccess() throws Exception {
        doNothing().when(mockKafkaClient).send(anyString(),anyString(),any());
        String requestBody = getSearchRequest();
        MvcResult mvcResult = mockMvc.perform(post(HCX_SEARCH).content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        assertEquals(202, status);
    }

    private String getSearchRequest() throws JsonProcessingException {
        Map<String,Object> obj = new HashMap<>();
        obj.put("payload","eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAiLCJ4LWhjeC1zZW5kZXJfY29kZSI6IjEtODA1MDBjZGQtMmRlYy00ZDYwLWJkMWItOGY5ZDgzZjQ5N2ZmIiwieC1oY3gtcmVjaXBpZW50X2NvZGUiOiJoY3gtZ2F0ZXdheS1jb2RlIiwieC1oY3gtY29ycmVsYXRpb25faWQiOiIxZTgzLTQ2MGEtNGYwYi1iMDE2LWMyMmQ4MjA2NzRlMSIsIngtaGN4LWFwaV9jYWxsX2lkIjoiMjZiMTA2MGMtMWU4My00NjAwLTk2MTItZWEzMWUwY2E1MDkxIiwieC1oY3gtdGltZXN0YW1wIjoiMjAyMi0wMS0xNlQwOTo1MDoyMyswMCIsIngtaGN4LXN0YXR1cyI6InJlcXVlc3QuaW5pdGlhdGUiLCJ4LWhjeC1kZWJ1Z19mbGFnIjoiSW5mbyIsIngtaGN4LWVycm9yX2RldGFpbHMiOnsiZXJyb3IuY29kZSI6ImJhZC5pbnB1dCIsImVycm9yLm1lc3NhZ2UiOiJQcm92aWRlciBjb2RlIG5vdCBmb3VuZCIsInRyYWNlIjoiIn0sIngtaGN4LWRlYnVnX2RldGFpbHMiOnsiZXJyb3IuY29kZSI6ImJhZC5pbnB1dCIsImVycm9yLm1lc3NhZ2UiOiJQcm92aWRlciBjb2RlIG5vdCBmb3VuZCIsInRyYWNlIjoiIn0sIngtaGN4LXNlYXJjaCI6eyJmaWx0ZXJzIjp7InNlbmRlcnMiOlsiMS04MDUwMGNkZC0yZGVjLTRkNjAtYmQxYi04ZjlkODNmNDk3ZmYiXSwicmVjZWl2ZXJzIjpbIjEtOTNmOTA4YmEtYjU3OS00NTNlLThiMmEtNTYwMjJhZmFkMjc1Il0sImVudGl0eV90eXBlcyI6WyJwcmVhdXRoIiwiY2xhaW0iXSwid29ya2Zsb3dfaWRzIjpbXSwiY2FzZV9pZHMiOltdLCJlbnRpdHlfc3RhdHVzIjpbImNsYWltcy5jb21wbGV0ZWQiLCJjbGFpbXMucmVqZWN0ZWQiXX0sInRpbWVfcGVyaW9kIjoyNH0sImp3c19oZWFkZXIiOnsidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifSwiandlX2hlYWRlciI6eyJhbGciOiJSU0EtT0FFUCIsImVuYyI6IkEyNTZHQ00ifX0=.6KB707dM9YTIgHtLvtgWQ8mKwboJW3of9locizkDTHzBC2IlrT1oOQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.AxY8DCtDaGlsbGljb3RoZQ.KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY.Mz-VPPyU4RlcuYv1IwIvzw");
        return JSONUtils.serialize(obj);
    }

    @Test
    public void onSearchBadRequest() throws Exception {
        when(headerAuditService.search(any())).thenReturn(Arrays.asList(new HeaderAudit("AUDIT", new Object(), new Object(), "1-2799b6a4-cf2d-45fe-a5e1-5f1c82979e0d", "93f908ba", "ff84928c-a077-4565-8fb1-731b1b6466a0", "", "1e83-460a-4f0b-b016-c22d820674e1", "2022-01-06T09:50:23+00", new Long("1642781095099"), new Long("1642781095099"), new Long("1642781095099"), "/v1/hcx/search", "59cefda2-a4cc-4795-95f3-fb9e82e21cef", "request.queued")));
        doNothing().when(mockKafkaClient).send(anyString(),anyString(),any());
        String requestBody = getOnSearchBadRequest();
        MvcResult mvcResult = mockMvc.perform(post(HCX_ON_SEARCH).content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        assertEquals(400, status);
    }

    private String getOnSearchBadRequest() throws JsonProcessingException {
        Map<String,Object> obj = new HashMap<>();
        obj.put("payload","eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAiLCJ4LWhjeC1zZW5kZXJfY29kZSI6IjEtOTNmOTA4YmEtYjU3OS00NTNlLThiMmEtNTYwMjJhZmFkMjc1IiwieC1oY3gtcmVjaXBpZW50X2NvZGUiOiJoY3gtZ2F0ZXdheS1jb2RlIiwieC1oY3gtd29ya2Zsb3dfaWQiOiIxZTgzLTQ2MGEtNGYwYi1iMDE2LWMyMmQ4MjA2NzRlMSIsIngtaGN4LWFwaV9jYWxsX2lkIjoiZmY4NDkyOGMtYTA3Ny00NTY1LThmYjEtNzMxYjFiNjQ2NmEwIiwieC1oY3gtc3RhdHVzIjoicmVzcG9uc2UuaW5pdGlhdGUiLCJ4LWhjeC1kZWJ1Z19mbGFnIjoiSW5mbyIsIngtaGN4LWVycm9yX2RldGFpbHMiOnsiZXJyb3IuY29kZSI6ImJhZC5pbnB1dCIsImVycm9yLm1lc3NhZ2UiOiJQcm92aWRlciBjb2RlIG5vdCBmb3VuZCIsInRyYWNlIjoiIn0sIngtaGN4LWRlYnVnX2RldGFpbHMiOnsiZXJyb3IuY29kZSI6ImJhZC5pbnB1dCIsImVycm9yLm1lc3NhZ2UiOiJQcm92aWRlciBjb2RlIG5vdCBmb3VuZCIsInRyYWNlIjoiIn0sIngtaGN4LXNlYXJjaF9yZXNwb25zZSI6e319.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ");
        return JSONUtils.serialize(obj);
    }

    @Test
    public void onSearchSuccess() throws Exception {
        when(headerAuditService.search(any())).thenReturn(Arrays.asList(new HeaderAudit("AUDIT", new Object(), new Object(), "1-2799b6a4-cf2d-45fe-a5e1-5f1c82979e0d", "93f908ba", "ff84928c-a077-4565-8fb1-731b1b6466a0", "", "1e83-460a-4f0b-b016-c22d820674e1", "2022-01-06T09:50:23+00", new Long("1642781095099"), new Long("1642781095099"), new Long("1642781095099"), "/v1/hcx/search", "59cefda2-a4cc-4795-95f3-fb9e82e21cef", "request.queued")));
        doNothing().when(mockKafkaClient).send(anyString(),anyString(),any());
        String requestBody = getOnSearchRequest();
        MvcResult mvcResult = mockMvc.perform(post(HCX_ON_SEARCH).content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        assertEquals(202, status);
    }

    private String getOnSearchRequest() throws JsonProcessingException {
        Map<String,Object> obj = new HashMap<>();
        obj.put("payload","eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAiLCJ4LWhjeC1zZW5kZXJfY29kZSI6IjEtOTNmOTA4YmEtYjU3OS00NTNlLThiMmEtNTYwMjJhZmFkMjc1IiwieC1oY3gtcmVjaXBpZW50X2NvZGUiOiJoY3gtZ2F0ZXdheS1jb2RlIiwieC1oY3gtY29ycmVsYXRpb25faWQiOiIxZTgzLTQ2MGEtNGYwYi1iMDE2LWMyMmQ4MjA2NzRlMSIsIngtaGN4LWFwaV9jYWxsX2lkIjoiZmY4NDkyOGMtYTA3Ny00NTY1LThmYjEtNzMxYjFiNjQ2NmEwIiwieC1oY3gtdGltZXN0YW1wIjoiMjAyMi0wMS0xNlQwOTo1MDoyMyswMCIsIngtaGN4LXN0YXR1cyI6InJlc3BvbnNlLnN1Y2Nlc3MiLCJ4LWhjeC1kZWJ1Z19mbGFnIjoiSW5mbyIsIngtaGN4LWVycm9yX2RldGFpbHMiOnsiZXJyb3IuY29kZSI6ImJhZC5pbnB1dCIsImVycm9yLm1lc3NhZ2UiOiJQcm92aWRlciBjb2RlIG5vdCBmb3VuZCIsInRyYWNlIjoiIn0sIngtaGN4LWRlYnVnX2RldGFpbHMiOnsiZXJyb3IuY29kZSI6ImJhZC5pbnB1dCIsImVycm9yLm1lc3NhZ2UiOiJQcm92aWRlciBjb2RlIG5vdCBmb3VuZCIsInRyYWNlIjoiIn0sIngtaGN4LXNlYXJjaF9yZXNwb25zZSI6eyJjb3VudCI6NiwiZW50aXR5X2NvdW50cyI6eyJjbGFpbSI6MSwicHJlYXV0aCI6MiwicHJlZGV0ZXJtaW5hdGlvbiI6M319LCJqd3NfaGVhZGVyIjp7InR5cCI6IkpXVCIsImFsZyI6IlJTMjU2In0sImp3ZV9oZWFkZXIiOnsiYWxnIjoiUlNBLU9BRVAiLCJlbmMiOiJBMjU2R0NNIn19.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ");
        return JSONUtils.serialize(obj);
    }


}
