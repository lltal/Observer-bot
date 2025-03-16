@org.hibernate.annotations.GenericGenerator(
        name = DbConstants.GENERATOR_ID,
        strategy = "enhanced-sequence",
        parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "optimizer",
                        value = "pooled-lo"
                ),
                @org.hibernate.annotations.Parameter(
                        name = "initial_value",
                        value = "1"
                ),
                @org.hibernate.annotations.Parameter(
                        name = "increment_size",
                        value = "5"
                )
        }
)
package com.github.lltal.observer.model;

