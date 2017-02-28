satisfactionAction(AGENT, greeting, positive_high) :- other(AGENT).
satisfactionAction(AGENT, smile, positive_low) :- other(AGENT).
satisfactionAction(AGENT, run, negative_low) :- other(AGENT).
satisfactionAction(AGENT, bye, negative_high) :- other(AGENT).
satisfactionAction(AGENT, eat, positive_high) :- self(AGENT).
satisfactionAction(AGENT, sleep, positive_low) :- self(AGENT).
satisfactionAction(AGENT, wake, negative_low) :- self(AGENT).
satisfactionAction(AGENT, pay, negative_high) :- self(AGENT).
satisfactionObject(AGENT, PROPERTIES, positive_high) :- member(color=blue, PROPERTIES), anyone(AGENT).
satisfactionObject(AGENT, PROPERTIES, positive_low) :- member(color=red, PROPERTIES), anyone(AGENT).
satisfactionObject(AGENT, PROPERTIES, negative_low) :- member(color=white, PROPERTIES), anyone(AGENT).
satisfactionObject(AGENT, PROPERTIES, negative_high) :- member(color=black, PROPERTIES), anyone(AGENT).