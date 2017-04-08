valence(AGENT, reputationIncrease, positive, positive) :- self(AGENT).
valence(AGENT, reputationDecrease, negative, negative) :- self(AGENT).
valence(AGENT, r1, positive, negative) :- self(AGENT).
valence(AGENT, r2, negative, positive) :- self(AGENT).