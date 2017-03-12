/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import util.MessageBuilder;
import util.StopWatch;

public class ProtocolAssistant {

    private Agent agent;
    private long timeout;
    private StopWatch stopWatch;

    public ProtocolAssistant(Agent agent) {
        this(agent, 5000);
    }

    public ProtocolAssistant(Agent agent, long timeout) {
        this.agent = agent;
        this.timeout = timeout;
        stopWatch = new StopWatch();
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public ACLMessage createRequestMessage(AID receiver, String content) {
        return new MessageBuilder()
                .request()
                .sender(agent.getAID())
                .receiver(receiver)
                .fipaRequest()
                .conversationId()
                .replyWith()
                .content(content)
                .build();
    }

    public ACLMessage sendRequest(ACLMessage request) {
        agent.send(request);
        MessageTemplate responseTemplate = MessageTemplate.MatchAll();
        if (request.getReplyWith() != null) {
            responseTemplate = MessageTemplate.MatchInReplyTo(request.getReplyWith());
        }
        stopWatch.reset();
        stopWatch.start();
        ACLMessage response = agent.blockingReceive(responseTemplate, timeout);
        stopWatch.stop();
        verifyResponse(response);
        if (response.getPerformative() == ACLMessage.AGREE) {
            response = agent.blockingReceive(responseTemplate, timeout - stopWatch.getTime());
        }
        verifyResponse(response);
        return response;
    }

    public ACLMessage sendRequest(ACLMessage request, int expectedPerformative) {
        ACLMessage response = sendRequest(request);
        if (response.getPerformative() != expectedPerformative) {
            throw new InvalidResponseException(String.format("Expected performative: %s and was: %s", ACLMessage.getPerformative(expectedPerformative), ACLMessage.getPerformative(response.getPerformative())));
        }
        return response;
    }

    private void verifyResponse(ACLMessage response) {
        if (response == null) {
            throw new TimeoutRequestException("The agent did not respond");
        }
    }

}
