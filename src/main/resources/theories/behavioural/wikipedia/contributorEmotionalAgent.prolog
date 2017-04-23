valence(AGENT, highReputationIncrease, positive, positive) :- self(AGENT).
valence(AGENT, neutralReputationIncrease, neutral, positive) :- self(AGENT).
valence(AGENT, lowReputationIncrease, negative, positive) :- self(AGENT).
valence(AGENT, highReputationDecrease, negative, negative) :- self(AGENT).
valence(AGENT, neutralReputationDecrease, neutral, negative) :- self(AGENT).
valence(AGENT, lowReputationDecrease, positive, negative) :- self(AGENT).