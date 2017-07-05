package service;

import models.EpicVO;
import models.SubTaskVO;
import models.UserStoryVO;
import ninja.session.Session;
import noNamespace.RssDocument;
import org.apache.xmlbeans.XmlException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by nnthat on 3/4/17.
 */
public class XmlReleaseWithUrlService {
    private static final String EPIC = "Epic";
    private static final String STORY = "Story";

    public static Map<String, Object> getDataFromGreenHopper(String xml) throws XmlException {
        Map<String, Object> releaseJsonData = new HashMap<>();

        RssDocument rssDocument = RssDocument.Factory.parse(xml);
        RssDocument.Rss rssNode = rssDocument.getRss();
        RssDocument.Rss.Channel channelNode = rssNode.getChannel();
        RssDocument.Rss.Channel.Item[] itemsArray = channelNode.getItemArray();
        List<RssDocument.Rss.Channel.Item> itemList = Arrays.asList(itemsArray);
        String fixVersion = itemsArray[0].getFixVersion();
        Map<String, Set<Object>> productList = new HashMap<>();
        List<EpicVO> epicList = new ArrayList<>();
        List<UserStoryVO> userStoryList = new ArrayList<>();
        List<SubTaskVO> subTaskList = new ArrayList<>();
        List<EpicVO> listNotCreatedEpics = new ArrayList<>();
        for (RssDocument.Rss.Channel.Item item : itemList) {
            String type = item.getType();
            EpicVO newEpic = new EpicVO();
            String project = item.getProject().getStringValue();
            String key = item.getKey();
            String summary = item.getSummary();
            String priority = item.getPriority();
            String status = item.getStatus();
            String resolution = item.getResolution();
            String security = item.getSecurity();
            String parent = item.getParent();
            String assignee = item.getAssignee();
            String reporter = item.getReporter();
            String created = item.getCreated();
            String updated = item.getUpdated();
            String resolved = item.getResolved();
            String votes = item.getVotes();
            String watches = item.getWatches();
            List<String> component = Arrays.asList(item.getComponentArray());
            List<String> listSubTask = Arrays.asList(item.getSubtasks().getSubtaskArray());

            List<String> listOfCustomFieldName = new ArrayList<>();
            listOfCustomFieldName.add("Epic Priority");
            listOfCustomFieldName.add("Feature Owner");
            listOfCustomFieldName.add("Implementation Authority");
            listOfCustomFieldName.add("Number of user stories");
            listOfCustomFieldName.add("PRODUCT");
            listOfCustomFieldName.add("Story Points");
            listOfCustomFieldName.add("Epic Link");
            listOfCustomFieldName.add("Sprint");
            listOfCustomFieldName.add("Wiki Reviewed Date");
            listOfCustomFieldName.add("Wiki Estimate");
            listOfCustomFieldName.add("Target Wiki Reviewed Date");

            Map<String, String> customFieldValueMap = getValueOfCustomField(listOfCustomFieldName, item.getCustomfields().getCustomfieldArray());
            String epicPriority = customFieldValueMap.get("Epic Priority");
            if (epicPriority != null) {
                epicPriority = epicPriority.replace(".0", "");
            }
            String fo = customFieldValueMap.get("Feature Owner");
            String ia = customFieldValueMap.get("Implementation Authority");
            String numberOfUs = customFieldValueMap.get("Number of user stories");
            String product = customFieldValueMap.get("PRODUCT");
            String storyPoints = customFieldValueMap.get("Story Points");
            String epicLink = customFieldValueMap.get("Epic Link");
            String sprint = customFieldValueMap.get("Sprint");
            String wikiReviewData = customFieldValueMap.get("Wiki Reviewed Date");
            String wikiEstimate = customFieldValueMap.get("Wiki Estimate");
            String targetWikiReviewedDate = customFieldValueMap.get("Target Wiki Reviewed Date");
            if (type.equals(EPIC)) {
                newEpic.setProject(project);
                newEpic.setKey(key);
                newEpic.setSummary(summary);
                newEpic.setPriority(priority);
                newEpic.setStatus(status);
                newEpic.setResolution(resolution);
                newEpic.setSecurity(security);
                newEpic.setAssignee(assignee);
                newEpic.setReporter(reporter);
                newEpic.setCreated(created);
                newEpic.setUpdated(updated);
                newEpic.setResolved(resolved);
                newEpic.setVotes(votes);
                newEpic.setWatches(watches);
                newEpic.setEpicPriority(epicPriority);
                newEpic.setFo(fo);
                newEpic.setIa(ia);
                newEpic.setNumberOfUS(numberOfUs);

                newEpic.setProduct(product);
                newEpic.setType(type);
                newEpic.setStoryPoints(storyPoints);
                newEpic.setWikiEstimate(wikiEstimate);
                newEpic.setTargetWikiReviewedDate(targetWikiReviewedDate);

                String componentValue = "AAAA";
                if (component.size() >= 1) {
                    componentValue = component.get(component.size() - 1);
                    newEpic.setComponent(componentValue);
                } else {
                    component = new ArrayList<>();
                    component.add(componentValue);
                    newEpic.setComponent(componentValue);
                }

                Set<Object> componentList = null;
                if (productList.containsKey(product)) {
                    componentList = productList.get(product);
                } else {
                    componentList = new HashSet<>();
                }
                componentList.addAll(component);
                productList.put(product, componentList);


                epicList.add(newEpic);

                if (newEpic.getStatus().equals("LA Needed") || newEpic.getStatus().equals("Ready for Selection") || newEpic.getStatus().equals("Selected")) {
                    if (newEpic.getNumberOfUS() == null) {
                        newEpic.setNumberOfUS("1.0");
                    }
                    listNotCreatedEpics.add(newEpic);
                }
            } else if (type.equals(STORY)) {
                UserStoryVO newUserStory = new UserStoryVO();
                newUserStory.setProject(project);
                newUserStory.setKey(key);
                newUserStory.setSummary(summary);
                newUserStory.setPriority(priority);
                newUserStory.setStatus(status);
                newUserStory.setEpicPriority(epicPriority);
                newUserStory.setFo(fo);
                newUserStory.setIa(ia);
                newUserStory.setNumberOfUS(numberOfUs);
                newUserStory.setProduct(product);
                newUserStory.setType(type);
                newUserStory.setStoryPoints(storyPoints);
                newUserStory.setAssignee(assignee);
                newUserStory.setEpicLink(epicLink);
                newUserStory.setSprint(sprint);
                newUserStory.setWikiReviewedDate(wikiReviewData);
                newUserStory.setListSubTask(listSubTask);
                userStoryList.add(newUserStory);
            } else {
                SubTaskVO newSubTask = new SubTaskVO();
                newSubTask.setKey(item.getKey());
                if (status.equals("Closed") || status.equals("Resolved")) {
                    newSubTask.setStatus("Done");
                } else {
                    newSubTask.setStatus(status);
                }
                newSubTask.setParent(parent);
                subTaskList.add(newSubTask);
            }
        }
        List<UserStoryVO> listUsRemove = new ArrayList<>();

        for (UserStoryVO usTemp : userStoryList) {
            String epicLink = usTemp.getEpicLink();
            boolean found = false;
            if (epicLink == null) {
                listUsRemove.add(usTemp);
                continue;
            }
            for (EpicVO epicTemp : epicList) {
                String epicKey = epicTemp.getKey();
                if (epicLink.equals(epicKey)) {
                    List<String> listUSofEpic = epicTemp.getUserStoryList();
                    if (listUSofEpic == null) {
                        listUSofEpic = new ArrayList<>();
                    }
                    listUSofEpic.add(usTemp.getKey());
                    epicTemp.setUserStoryList(listUSofEpic);
                    found = true;
                    break;
                }
            }
            if (!found) listUsRemove.add(usTemp);
        }
        userStoryList.removeAll(listUsRemove);

        for (EpicVO epicTemp : listNotCreatedEpics) {
            String numberString = epicTemp.getNumberOfUS();
            int start;
            int numberOfUserStory = Math.round(Float.valueOf(numberString));
            start = 0;
            for (UserStoryVO userStoryVOTemp : userStoryList) {
                String epicLinkTemp = userStoryVOTemp.getEpicLink();
                if (epicLinkTemp.equals(epicTemp.getKey())) {
                    start++;
                }
            }

            for (int i = start; i < numberOfUserStory; i++) {
                UserStoryVO newUserStory = new UserStoryVO();
                newUserStory.setProject(epicTemp.getProject());
                newUserStory.setStatus("Not Created");
                newUserStory.setProduct(epicTemp.getProduct());
                newUserStory.setEpicLink(epicTemp.getKey());
                String minus = "-";
                newUserStory.setKey(minus);
                newUserStory.setSummary(minus);
                newUserStory.setAssignee(minus);
                newUserStory.setStoryPoints(minus);
                newUserStory.setWikiReviewedDate(minus);
                userStoryList.add(newUserStory);
            }

        }
        Map<String, Object> newProductList = new LinkedHashMap<>();
        Map<String, Set<Object>> sortedProductList = new LinkedHashMap<>();
        productList.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(product -> {
            Set<Object> temp = new TreeSet<>(product.getValue());
            sortedProductList.put(product.getKey(), temp);
        });
        sortedProductList.forEach((product, components) -> {

            Map<String, Object> newComponentList = new HashMap<>();
            Map<String, Set<EpicVO>> data = new LinkedHashMap<>();
            components.forEach(component -> {
                Set<EpicVO> newEpicList = epicList.stream().filter(epic ->
                        epic.getProduct().equals(product)
                                && epic.getComponent().equals(component)
                ).collect(Collectors.toSet());
                newEpicList = filterUserStoriesAndSubTasks(newEpicList, userStoryList, subTaskList);
                data.put(component.toString(), newEpicList);
            });
//            List<EpicVO> epicListByProduct = epicList.stream().filter(epicVO -> epicVO.getProduct().equals(product)).collect(Collectors.toList());
//            List<UserStoryVO> usListByProduct = userStoryList.stream().filter(usVO -> usVO.getProduct().equals(product)).collect(Collectors.toList());
            Map<String, Object> countResults = countEpicsAndUserStories(data);
            newComponentList.put("overall", countResults);
            newComponentList.put("data", data);
            newProductList.put(product, newComponentList);
        });
        Map<String, Object> releaseData = new HashMap<>();
        releaseData.put("listProduct", newProductList);

//        releaseData.put("userStoryList", userStoryList);
//        releaseData.put("subTaskList", subTaskList);
        releaseJsonData.put("fixVersion", fixVersion);
        releaseJsonData.put("data", releaseData);

        return releaseJsonData;
    }

    private static Map<String, String> getValueOfCustomField(List<String> customFieldNames, RssDocument.Rss.Channel.Item.Customfields.Customfield[] customfieldArray) {
        String value = "";
        Map<String, String> results = new HashMap<>();
        for (RssDocument.Rss.Channel.Item.Customfields.Customfield current : customfieldArray) {
            String name = current.getCustomfieldname();
            for (String temp : customFieldNames) {
                if (temp.equals(name)) {
                    value = current.getCustomfieldvalues().getCustomfieldvalue();
                    results.put(temp, value);
                }
            }
        }
        return results;
    }

//    private static Map<String, Object> countEpicsAndUserStories(String product, List<EpicVO> epicList, List<UserStoryVO> userStoryList) {
//        Map<String, Object> results = new HashMap<>();
//        int epicTotal;
//        int usTotal;
//
//        Map<String, String> listEpicStatus = new HashMap<>();
//        listEpicStatus.put("todo", "todo");
//        listEpicStatus.put("laNeeded", "LA Needed");
//        listEpicStatus.put("readyForSelection", "Ready for Selection");
//        listEpicStatus.put("selected", "Selected");
//        listEpicStatus.put("defined", "Defined");
//        listEpicStatus.put("done", "Done");
//        Map<String, Integer> epicCountResults = new HashMap<>();
//        listEpicStatus.forEach((key, value) -> {
//            List<EpicVO> tempList = epicList.stream().filter(epicVO -> epicVO.getProduct().equals(product) && epicVO.getStatus().equals(value)).collect(Collectors.toList());
//            int tempCount = tempList.size();
//            epicList.removeAll(tempList);
//            epicCountResults.put(key, tempCount);
//        });
//
//        Map<String, String> listUSStatus = new HashMap<>();
//        listUSStatus.put("notCreated", "Not Created");
//        listUSStatus.put("todo", "todo");
//        listUSStatus.put("defined", "Defined");
//        listUSStatus.put("inProgress", "In Progress");
//        listUSStatus.put("inReview", "In Review");
//        listUSStatus.put("done", "Done");
//        Map<String, Integer> usCountResults = new HashMap<>();
//        listUSStatus.forEach((key, value) -> {
//            List<UserStoryVO> tempList = userStoryList.stream().filter(userStoryVO -> userStoryVO.getProduct().equals(product) && userStoryVO.getStatus().equals(value)).collect(Collectors.toList());
//            int tempCount = tempList.size();
//            userStoryList.removeAll(tempList);
//            usCountResults.put(key, tempCount);
//        });
//        epicTotal = epicCountResults.values().stream().mapToInt(Integer::intValue).sum();
//        usTotal = usCountResults.values().stream().mapToInt(Integer::intValue).sum();
//        results.put("epicTotal", epicTotal);
//        results.put("usTotal", usTotal);
//        results.put("epicCount", epicCountResults);
//        results.put("userStoryCount", usCountResults);
//        return results;
//    }

    private static Map<String, Object> countEpicsAndUserStories(Map<String, Set<EpicVO>> data) {
        Map<String, Object> results = new HashMap<>();
        int epicTotal;
        int usTotal;

        Set<EpicVO> epicList = data.values().stream().flatMap(x -> x.stream()).collect(Collectors.toSet());
        Map<String, String> listEpicStatus = new HashMap<>();
        listEpicStatus.put("todo", "todo");
        listEpicStatus.put("laNeeded", "LA Needed");
        listEpicStatus.put("readyForSelection", "Ready for Selection");
        listEpicStatus.put("selected", "Selected");
        listEpicStatus.put("defined", "Defined");
        listEpicStatus.put("done", "Done");
        Map<String, Integer> epicCountResults = new HashMap<>();
        listEpicStatus.forEach((key, value) -> {
            List<EpicVO> tempList = epicList.stream().filter(epicVO -> epicVO.getStatus().equals(value)).collect(Collectors.toList());
            int tempCount = tempList.size();
            epicCountResults.put(key, tempCount);
        });
        List<UserStoryVO> userStoryList = new ArrayList<>();
        epicList.stream().forEach(epicVO -> {
            userStoryList.addAll(epicVO.getUserStoryVOS());
        });
        Map<String, String> listUSStatus = new HashMap<>();
        listUSStatus.put("notCreated", "Not Created");
        listUSStatus.put("todo", "todo");
        listUSStatus.put("defined", "Defined");
        listUSStatus.put("inProgress", "In Progress");
        listUSStatus.put("inReview", "In Review");
        listUSStatus.put("done", "Done");
        Map<String, Integer> usCountResults = new HashMap<>();
        listUSStatus.forEach((key, value) -> {
            List<UserStoryVO> tempList = userStoryList.stream().filter(userStoryVO -> userStoryVO.getStatus().equals(value)).collect(Collectors.toList());
            int tempCount = tempList.size();
            usCountResults.put(key, tempCount);
        });
        epicTotal = epicCountResults.values().stream().mapToInt(Integer::intValue).sum();
        usTotal = usCountResults.values().stream().mapToInt(Integer::intValue).sum();
        results.put("epicTotal", epicTotal);
        results.put("usTotal", usTotal);
        results.put("epicCount", epicCountResults);
        results.put("userStoryCount", usCountResults);
        return results;
    }

    private static Set<EpicVO> filterUserStoriesAndSubTasks(Set<EpicVO> epicList, List<UserStoryVO> userStoryList, List<SubTaskVO> subTaskList) {
        epicList.forEach(epicVO -> {
            List<UserStoryVO> newUserStoryList = userStoryList.stream().filter(userStoryVO -> userStoryVO.getEpicLink().equals(epicVO.getKey())).collect(Collectors.toList());
            newUserStoryList.forEach(userStoryVO -> {
                List<String> subTaskKeyList = userStoryVO.getListSubTask();
                Map<String, Object> subTaskCounter = new HashMap<>();
                if (subTaskKeyList != null) {
                    List<SubTaskVO> newSubTaskList = subTaskList.stream().filter(subTaskVO -> subTaskKeyList.contains(subTaskVO.getKey())).collect(Collectors.toList());
                    Map<String, String> listSubTaskStatus = new HashMap<>();
                    listSubTaskStatus.put("open", "Open");
                    listSubTaskStatus.put("inProgress", "In Progress");
                    listSubTaskStatus.put("inReview", "In Review");
                    listSubTaskStatus.put("done", "Done");
                    Map<String, Object> subTaskCountResults = new HashMap<>();
                    listSubTaskStatus.forEach((key, value) -> {
                        long number = newSubTaskList.stream().filter(subTaskVO -> subTaskVO.getStatus().equals(value)).count();
                        subTaskCountResults.put(key, number);
                    });
                    subTaskCounter.put("subTaskCountResults", subTaskCountResults);
                }
                userStoryVO.setSubTasksCounter(subTaskCounter);
            });
            Map<String, String> listUSStatus = new HashMap<>();
            listUSStatus.put("notCreated", "Not Created");
            listUSStatus.put("notDefined", "Not Defined");
            listUSStatus.put("defined", "Defined");
            listUSStatus.put("inProgress", "In Progress");
            listUSStatus.put("inReview", "In Review");
            listUSStatus.put("done", "Done");
            Map<String, Object> usCounterResults = new HashMap<>();
            listUSStatus.forEach((key, value) -> {
                long number = newUserStoryList.stream().filter(userStoryVO -> userStoryVO.getStatus().equals(value)).count();
                usCounterResults.put(key, number);
            });
            epicVO.setUserStoryCounter(usCounterResults);
            epicVO.setUserStoryVOS(newUserStoryList);
        });
        return epicList;
    }
}
