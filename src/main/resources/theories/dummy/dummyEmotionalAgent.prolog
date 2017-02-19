satisfaction(AGENT, greeting, positive_high) :- other(AGENT).
satisfaction(AGENT, smile, positive_low) :- other(AGENT).
satisfaction(AGENT, run, negative_low) :- other(AGENT).
satisfaction(AGENT, bye, negative_high) :- other(AGENT).
satisfaction(AGENT, eat, positive_high) :- self(AGENT).
satisfaction(AGENT, sleep, positive_low) :- self(AGENT).
satisfaction(AGENT, wake, negative_low) :- self(AGENT).
satisfaction(AGENT, pay, negative_high) :- self(AGENT).
