activation(AGENT, hello, 0.6) :- other(AGENT).
satisfaction(AGENT, hello, 0.6) :- other(AGENT).
activation(AGENT, bye, -0.6) :- other(AGENT).
satisfaction(AGENT, bye, -0.6) :- other(AGENT).
activation(AGENT, sleep, 0.4) :- self(AGENT).
satisfaction(AGENT, sleep, 0.4) :- self(AGENT).