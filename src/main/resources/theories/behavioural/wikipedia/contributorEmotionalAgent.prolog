stimulus(AGENT, highReputationIncrease, 0.3, 0.3) :- self(AGENT).
stimulus(AGENT, mediumReputationIncrease, 0, 0.1) :- self(AGENT).
stimulus(AGENT, lowReputationIncrease, -0.05, 0.05) :- self(AGENT).
stimulus(AGENT, highReputationDecrease, -0.3, -0.3) :- self(AGENT).
stimulus(AGENT, mediumReputationDecrease, 0, -0.1) :- self(AGENT).
stimulus(AGENT, lowReputationDecrease, -0.05, -0.05) :- self(AGENT).