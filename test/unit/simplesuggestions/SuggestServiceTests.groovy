package simplesuggestions

import com.nerderg.simpleSuggestions.ClasspathSuggestionLoader
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

import com.nerderg.simpleSuggestions.SuggestService
import org.junit.Test

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
class SuggestServiceTests {

    private SuggestService suggestService

    void setUp() {
    }

    @Test
    void testSuggestion() {
        def grailsApp = [config: [suggest: [data: [directory: './test/unit/simplesuggestions/suggestions']]]]
        def suggestService = new SuggestService(grailsApplication: grailsApp)
        def handler = { String term ->
            return [term, "$term A", "$term B"]
        }
        suggestService.addSuggestionHandler('test', handler)
        assert suggestService.getSuggestions('test', 'wally') == ['wally', 'wally A', 'wally B']
        assert suggestService.getSuggestions('test', 'hello') == ['hello', 'hello A', 'hello B']
        assert suggestService.getSuggestions('toast', 'hello') == []
        assert suggestService.getSuggestions('titles', 'M') == ['Mr', 'Ms', 'Miss', 'Mrs', 'Master']
        assert suggestService.getSuggestions('titles', 'D') == ['Dr']
    }

    @Test
    void testDefaultDirectory() {
        def grailsApp = [config: [:]]
        def suggestService = new SuggestService(grailsApplication: grailsApp)
        assert suggestService.getSuggestions('titles', 'M') == ['Monster', 'Magnifico']
        assert suggestService.getSuggestions('toast', 'hello') == []
    }

    @Test
    void testClasspathDirectory() {
        def suggestService = new SuggestService(suggestionLoader: new ClasspathSuggestionLoader("suggestions"))
        assert suggestService.getSuggestions('titles', 'N') == ['Nerd', 'Nerd Sr']
        assert suggestService.getSuggestions('toast', 'hello') == []
    }

    @Test
    void testClasspathDirectory2() {
        def suggestService = new SuggestService(suggestionLoader: new ClasspathSuggestionLoader("suggestions/"))
        assert suggestService.getSuggestions('titles', 'S') == ['SuperNerd']
        assert suggestService.getSuggestions('toast', 'hello') == []
    }
}
