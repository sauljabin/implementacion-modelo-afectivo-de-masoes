emotionType(admiration, positive).
emotionType(anger, negative_high).
emotionType(compassion, positive).
emotionType(depression, negative_high).
emotionType(happiness, positive).
emotionType(joy, positive).
emotionType(rejection, negative_low).
emotionType(sadness, negative_low).
behaviourPriority(EMOTION, imitative) :- emotionType(EMOTION, positive).
behaviourPriority(EMOTION, cognitive) :- emotionType(EMOTION, negative_low).
behaviourPriority(EMOTION, reactive) :- emotionType(EMOTION, negative_high).