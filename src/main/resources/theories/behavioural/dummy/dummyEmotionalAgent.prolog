valence(AGENT, greeting, negative, positive) :- other(AGENT).
valence(AGENT, smile, positive, negative) :- other(AGENT).
valence(AGENT, run, negative, negative) :- other(AGENT).
valence(AGENT, bye, negative, negative) :- other(AGENT).
valence(AGENT, eat, positive, positive) :- self(AGENT).
valence(AGENT, sleep, positive, positive) :- self(AGENT).
valence(AGENT, wake, negative, negative) :- self(AGENT).
valence(AGENT, pay, negative, negative) :- self(AGENT).