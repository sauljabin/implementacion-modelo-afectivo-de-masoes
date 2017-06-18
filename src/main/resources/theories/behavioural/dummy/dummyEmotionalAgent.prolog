stimulus(AGENT, greeting, 0.1, 0.1) :- other(AGENT).
stimulus(AGENT, smile, 0.5, 0.5) :- other(AGENT).
stimulus(AGENT, run, 0.2, -0.3) :- other(AGENT).
stimulus(AGENT, bye, -0.5, -0.5) :- other(AGENT).
stimulus(AGENT, eat, 0.5, 0.5) :- self(AGENT).
stimulus(AGENT, sleep, 0, 0.5) :- self(AGENT).
stimulus(AGENT, wake, -0.3, -0.5) :- self(AGENT).
stimulus(AGENT, pay, -0.3, -0.3) :- self(AGENT).