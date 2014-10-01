(ns re-demo.lists
  (:require [re-demo.util         :refer  [title]]
            [re-com.core          :refer  [label checkbox]]
            [re-com.box           :refer  [h-box v-box box gap line border]]
            [re-com.dropdown      :refer  [single-dropdown]]
            [reagent.core         :as     r]))

(defn- toggle-inclusion! [set-atom member]
  "convenience function to include/exclude member from"
  (reset! set-atom
          (if (contains? @set-atom member)
            (disj @set-atom member)
            (conj @set-atom member))))


(defn- parameters-with
  [content disabled?]
  (fn []
    [v-box
     :gap "20px"
     :align :start
     :children [[label :style {:font-style "italic"} :label "parameters:"]
                [h-box
                 :gap "20px"
                 :align :start
                 :children [[checkbox
                             :label ":disabled"
                             :model disabled?
                             :on-change #(reset! disabled? %)]]]

                content]]))



  (defn show-variant
    [variation]
    (let [disabled?    (r/atom false)
          label-style  {:font-style "italic" :font-size "smaller" :color "#777"}]
      (case variation
        "1" [(fn
               []
               [parameters-with
                [h-box
                 :gap "20px"
                 :align :start
                 :children [[(fn []
                               [v-box
                                :gap "5px"
                                :children [[label :style label-style :label "NOT YET IMPLEMENTED"]
                                           ]])]]]
                disabled?])]
        "2" [(fn
               []
               [parameters-with
                [h-box
                 :size "auto"
                 :align :start
                 :children [[(fn []
                               [v-box
                                :gap "5px"
                                :children [[label :style label-style :label "NOT YET IMPLEMENTED"]]])]]]
                disabled?])])))


(defn notes
  [_]
  [v-box
   :width "500px"
   :children [[:div.h4 "Parameters:"]
              [:div {:style {:font-size "small"}}
               [:label {:style {:font-variant "small-caps"}} "required"]
                [:ul
                 [:li.spacer [:strong ":model"]
                  " - TBA"]
                 [:li.spacer [:strong ":on-change"]
                  " - callback will be passed vector of selected items"]
                 ]
               [:label {:style {:font-variant "small-caps"}} "optional"]
                [:ul
                 [:li.spacer [:strong ":disabled"]
                  " - boolean can be reagent/atom. (default false) If true, scrolling is allowed but selection is disabled."]
                 [:li.spacer [:strong ":hide-border"]
                  " - boolean. Default false."]
                 ]]]])


(def variations [{:id "1" :label "Single selection"}
                 {:id "2" :label "Multiple selection"}])


(defn panel
  []
  (let [selected-variation (r/atom "1")]
    (fn []
      [v-box
       :children [[:h3.page-header "Single & Multiple Select List"]
                  [h-box
                   :gap "50px"
                   :children [[notes selected-variation]
                              [v-box
                               :gap "15px"
                               :size "auto"
                               :children  [[h-box
                                            :gap      "10px"
                                            :align    :center
                                            :children [[label :label "Select a variation"]
                                                       [single-dropdown
                                                        :options   variations
                                                        :model     selected-variation
                                                        :width     "300px"
                                                        :on-select #(reset! selected-variation %)]]]
                                           [gap :size "0px"] ;; Force a bit more space here
                                           [show-variant @selected-variation]]]]]]])))